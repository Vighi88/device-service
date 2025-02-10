package com.example.service;

import com.example.dao.DeviceDAO;
import com.example.model.DeviceRequest;
import com.hazelcast.map.MapStore;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceRequestMapStore implements MapStore<String, DeviceRequest> {
    private final DeviceDAO deviceDAO;

    public DeviceRequestMapStore(DeviceDAO deviceDAO) {
        this.deviceDAO = deviceDAO;
    }

    @Override
    public void store(String key, DeviceRequest value) {
        // Save to PostgreSQL
        deviceDAO.saveDeviceRequest(value);
    }

    @Override
    public void storeAll(Map<String, DeviceRequest> map) {
        map.forEach(this::store);
    }

    @Override
    public void delete(String key) {
        // Delete from PostgreSQL
        deviceDAO.deleteDeviceRequest(key);
    }

    @Override
    public void deleteAll(Collection<String> keys) {
        keys.forEach(this::delete);
    }

    @Override
    public DeviceRequest load(String key) {
        // Load from PostgreSQL
        return deviceDAO.loadDeviceRequest(key);
    }

    @Override
    public Map<String, DeviceRequest> loadAll(Collection<String> keys) {
        Map<String, DeviceRequest> result = new ConcurrentHashMap<>();
        keys.forEach(key -> result.put(key, load(key)));
        return result;
    }

    @Override
    public Iterable<String> loadAllKeys() {
        // Load all keys from PostgreSQL
        return deviceDAO.loadAllDeviceRequestKeys();
    }
}