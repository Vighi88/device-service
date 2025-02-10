package com.example;

import com.example.config.DeviceServiceConfiguration;
import com.example.config.HazelcastConfig;
import com.example.dao.DeviceDAO;
import com.example.resources.DeviceResource;
import com.example.service.DeviceRequestProcessor;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import com.hazelcast.core.HazelcastInstance;
import org.flywaydb.core.Flyway;
import io.dropwizard.jdbi3.JdbiFactory;
import org.jdbi.v3.core.Jdbi;

public class DeviceServiceApplication extends Application<DeviceServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new DeviceServiceApplication().run(args);
    }

    @Override
    public void run(DeviceServiceConfiguration configuration, Environment environment) {
        // Initialize Flyway
        Flyway flyway = Flyway.configure()
                .dataSource(
                        configuration.getDatabase().getUrl(),
                        configuration.getDatabase().getUser(),
                        configuration.getDatabase().getPassword()
                )
                .locations(configuration.getFlyway().getLocations())
                .baselineOnMigrate(configuration.getFlyway().isBaselineOnMigrate())  // Add this
                .baselineVersion(configuration.getFlyway().getBaselineVersion())     // Add this
                .load();
        flyway.migrate();

        // Initialize database
        final Jdbi jdbi = new JdbiFactory().build(environment, configuration.getDatabase(), "postgresql");
        final DeviceDAO deviceDAO = new DeviceDAO(jdbi);

        // Initialize Hazelcast with MapStore
        HazelcastInstance hazelcastInstance = HazelcastConfig.createHazelcastInstance(deviceDAO);

        // Register resources
        environment.jersey().register(new DeviceResource(hazelcastInstance, deviceDAO));

        // Start the processor
        new Thread(new DeviceRequestProcessor(hazelcastInstance, deviceDAO)).start();
    }
}