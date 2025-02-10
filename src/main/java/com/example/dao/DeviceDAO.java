package com.example.dao;

import com.example.model.DeviceRequest;
import org.jdbi.v3.core.Jdbi;
import java.util.List;
import java.util.Collections;

public class DeviceDAO {
    private final Jdbi jdbi;

    public DeviceDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    /**
     * Update the status and index of a device in the database.
     */
    public void updateDeviceStatus(String deviceId, String status, int index) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE devices SET status = :status, index = :index WHERE device_id = :deviceId")
                    .bind("status", status)
                    .bind("index", index)
                    .bind("deviceId", deviceId)
                    .execute();
        });
    }

    /**
     * Retrieve all waiting requests for a specific customer.
     */
    public List<DeviceRequest> getWaitingRequests(String customerId) {
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM device_requests WHERE customer_id = :customerId AND status = 'Waiting'")
                    .bind("customerId", customerId)
                    .mapToBean(DeviceRequest.class)
                    .list();
        });
    }

    /**
     * Save a device request to the database.
     */
    public void saveDeviceRequest(DeviceRequest request) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO device_requests (customer_id, country, device_id, action, status, created_timestamp, tracker_id, index, retry_count, last_retry_timestamp, timeout_timestamp) " +
                            "VALUES (:customerId, :country, :deviceId, :action, :status, :createdTimestamp, :trackerId, :index, :retryCount, :lastRetryTimestamp, :timeoutTimestamp)")
                    .bindBean(request)
                    .execute();
        });
    }

    /**
     * Delete a device request from the database by tracker ID.
     */
    public void deleteDeviceRequest(String trackerId) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM device_requests WHERE tracker_id = :trackerId")
                    .bind("trackerId", trackerId)
                    .execute();
        });
    }

    /**
     * Load a device request from the database by tracker ID.
     */
    public DeviceRequest loadDeviceRequest(String trackerId) {
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM device_requests WHERE tracker_id = :trackerId")
                    .bind("trackerId", trackerId)
                    .mapToBean(DeviceRequest.class)
                    .findOne()
                    .orElse(null);
        });
    }

    /**
     * Load all tracker IDs from the database.
     */
    public Iterable<String> loadAllDeviceRequestKeys() {
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT tracker_id FROM device_requests")
                    .mapTo(String.class)
                    .list();
        });
    }
}