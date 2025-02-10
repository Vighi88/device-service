package com.example;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Collections;

public class DeviceResourceTest {

    private static final HazelcastInstance hazelcastInstance = mock(HazelcastInstance.class);
    private static final DeviceDAO deviceDAO = mock(DeviceDAO.class);

    @Mock
    private IMap<String, DeviceRequest> requestMap;

    private final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new DeviceResource(hazelcastInstance, deviceDAO))
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(hazelcastInstance.getMap("device-requests")).thenReturn(requestMap);
    }

    @AfterEach
    void tearDown() {
        reset(hazelcastInstance, deviceDAO, requestMap);
    }

    @Test
    void testPatchDevice_NewRequest() {
        // Arrange
        DeviceRequest request = new DeviceRequest();
        request.setCustomerId("CUST123");
        request.setDeviceId("Device1");
        request.setAction("Pause");

        when(deviceDAO.getWaitingRequests("CUST123")).thenReturn(Collections.emptyList());

        // Act
        Response response = resources.target("/device/Device1")
                .request()
                .method("PATCH", Entity.json(request));

        // Assert
        assertEquals(202, response.getStatus());
        assertNotNull(response.getHeaderString("Tracker-ID"));
        verify(requestMap).put(anyString(), any(DeviceRequest.class));
    }

    @Test
    void testPatchDevice_ExistingRequest() {
        // Arrange
        DeviceRequest request = new DeviceRequest();
        request.setCustomerId("CUST123");
        request.setDeviceId("Device1");
        request.setAction("Pause");

        DeviceRequest waitingRequest = new DeviceRequest();
        waitingRequest.setTrackerId("TR123");

        when(deviceDAO.getWaitingRequests("CUST123")).thenReturn(Collections.singletonList(waitingRequest));

        // Act
        Response response = resources.target("/device/Device1")
                .request()
                .method("PATCH", Entity.json(request));

        // Assert
        assertEquals(202, response.getStatus());
        assertEquals("TR123", response.getHeaderString("Tracker-ID"));
        verify(requestMap, never()).put(anyString(), any(DeviceRequest.class));
    }
}