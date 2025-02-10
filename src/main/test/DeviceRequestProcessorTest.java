package com.example;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DeviceRequestProcessorTest {

    @Mock
    private HazelcastInstance hazelcastInstance;

    @Mock
    private DeviceDAO deviceDAO;

    @Mock
    private IMap<String, DeviceRequest> requestMap;

    private DeviceRequestProcessor processor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(hazelcastInstance.getMap("device-requests")).thenReturn(requestMap);
        processor = new DeviceRequestProcessor(hazelcastInstance, deviceDAO);
    }

    @Test
    void testProcessRequest_Success() {
        // Arrange
        DeviceRequest request = new DeviceRequest();
        request.setStatus("Waiting");
        request.setDeviceId("Device1");
        request.setAction("Pause");

        Map<String, DeviceRequest> requests = new HashMap<>();
        requests.put("TR123", request);

        when(requestMap.entrySet()).thenReturn(requests.entrySet());

        // Act
        Thread processorThread = new Thread(processor);
        processorThread.start();

        try {
            TimeUnit.SECONDS.sleep(1); // Wait for processor to run
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Assert
        verify(requestMap).put(eq("TR123"), any(DeviceRequest.class));
        verify(deviceDAO).updateDeviceStatus(eq("Device1"), eq("In-Progress"), anyInt());
        verify(requestMap).remove("TR123");
    }

    @Test
    void testProcessRequest_NoWaitingRequests() {
        // Arrange
        when(requestMap.entrySet()).thenReturn(Collections.emptySet());

        // Act
        Thread processorThread = new Thread(processor);
        processorThread.start();

        try {
            TimeUnit.SECONDS.sleep(1); // Wait for processor to run
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Assert
        verify(requestMap, never()).put(anyString(), any(DeviceRequest.class));
        verify(deviceDAO, never()).updateDeviceStatus(anyString(), anyString(), anyInt());
    }
}