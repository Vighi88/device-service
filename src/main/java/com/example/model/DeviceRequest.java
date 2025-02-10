package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class DeviceRequest {
    private String customerId;
    private String country;
    private String deviceId;
    private String action;
    private String status;
    private Instant createdTimestamp;
    private String trackerId;
    private int index;
    private int retryCount;
    private Instant lastRetryTimestamp;
    private Instant timeoutTimestamp;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Instant createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public Instant getLastRetryTimestamp() {
        return lastRetryTimestamp;
    }

    public void setLastRetryTimestamp(Instant lastRetryTimestamp) {
        this.lastRetryTimestamp = lastRetryTimestamp;
    }

    public Instant getTimeoutTimestamp() {
        return timeoutTimestamp;
    }

    public void setTimeoutTimestamp(Instant timeoutTimestamp) {
        this.timeoutTimestamp = timeoutTimestamp;
    }

    // Getters and Setters for all fields
}