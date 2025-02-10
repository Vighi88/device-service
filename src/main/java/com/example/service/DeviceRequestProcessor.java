package com.example.service;

import com.example.model.DeviceRequest;
import com.example.dao.DeviceDAO;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class DeviceRequestProcessor implements Runnable {
    private final HazelcastInstance hazelcastInstance;
    private final DeviceDAO deviceDAO;

    public DeviceRequestProcessor(HazelcastInstance hazelcastInstance, DeviceDAO deviceDAO) {
        this.hazelcastInstance = hazelcastInstance;
        this.deviceDAO = deviceDAO;
    }

    @Override
    public void run() {
        ConcurrentMap<String, DeviceRequest> requestMap = hazelcastInstance.getMap("device-requests");

        while (true) {
            requestMap.entrySet().stream()
                    .filter(entry -> "Waiting".equals(entry.getValue().getStatus()))
                    .forEach(entry -> {
                        DeviceRequest request = entry.getValue();
                        request.setStatus("In-Progress");
                        requestMap.put(entry.getKey(), request);

                        // Process the request (e.g., call ACS)
                        processRequest(request);

                        // Update database and remove from map
                        deviceDAO.updateDeviceStatus(request.getDeviceId(), request.getStatus(), request.getIndex());
                        requestMap.remove(entry.getKey());
                    });

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void processRequest(DeviceRequest request) {
        // Implement ACS call logic here
    }
}