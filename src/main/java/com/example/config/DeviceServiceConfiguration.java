package com.example.config;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DeviceServiceConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private FlywayConfiguration flyway = new FlywayConfiguration();

    // Getters and setters for database

    // Correct getter for "database" field
    @JsonProperty("database")
    public DataSourceFactory getDatabase() {
        return database;
    }

    // Correct setter for "database" field
    @JsonProperty("database")
    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }

    @JsonProperty("flyway")
    public FlywayConfiguration getFlyway() {
        return flyway;
    }

    @JsonProperty("flyway")
    public void setFlyway(FlywayConfiguration flyway) {
        this.flyway = flyway;
    }

    public static class FlywayConfiguration {
        private boolean enabled;
        private String[] locations;
        private boolean baselineOnMigrate;  // Add this
        private String baselineVersion;     // Add this

        // Getters and setters
        @JsonProperty("enabled")
        public boolean isEnabled() {
            return enabled;
        }

        @JsonProperty("enabled")
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @JsonProperty("locations")
        public String[] getLocations() {
            return locations;
        }

        @JsonProperty("locations")
        public void setLocations(String[] locations) {
            this.locations = locations;
        }

        @JsonProperty("baselineOnMigrate")
        public boolean isBaselineOnMigrate() {
            return baselineOnMigrate;
        }

        @JsonProperty("baselineOnMigrate")
        public void setBaselineOnMigrate(boolean baselineOnMigrate) {
            this.baselineOnMigrate = baselineOnMigrate;
        }

        @JsonProperty("baselineVersion")
        public String getBaselineVersion() {
            return baselineVersion;
        }

        @JsonProperty("baselineVersion")
        public void setBaselineVersion(String baselineVersion) {
            this.baselineVersion = baselineVersion;
        }
    }
}