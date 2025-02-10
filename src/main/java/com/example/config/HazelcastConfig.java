package com.example.config;

import com.example.service.DeviceRequestMapStore;
import com.example.dao.DeviceDAO;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Hazelcast;

public class HazelcastConfig {
    public static HazelcastInstance createHazelcastInstance(DeviceDAO deviceDAO) {
        Config config = new Config();
        config.getNetworkConfig().getRestApiConfig().setEnabled(true);
        MapConfig mapConfig = config.getMapConfig("device-requests");
        MapStoreConfig mapStoreConfig = new MapStoreConfig();
        mapStoreConfig.setImplementation(new DeviceRequestMapStore(deviceDAO));
        mapConfig.setMapStoreConfig(mapStoreConfig);
        return Hazelcast.newHazelcastInstance(config);
    }
}