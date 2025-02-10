CREATE TABLE device_requests (
    customer_id VARCHAR(255),
    country VARCHAR(255),
    device_id VARCHAR(255),
    action VARCHAR(255),
    status VARCHAR(255),
    created_timestamp TIMESTAMP,
    tracker_id VARCHAR(255) PRIMARY KEY,
    index INT,
    retry_count INT DEFAULT 0,
    last_retry_timestamp TIMESTAMP,
    timeout_timestamp TIMESTAMP
);