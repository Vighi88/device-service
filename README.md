# Device Service

A microservice built using Dropwizard to manage device pause/unpause operations. It integrates with Hazelcast for caching and PostgreSQL for persistence.

## Features
- REST API for pausing/unpausing devices.
- Hazelcast for request buffering and processing.
- PostgreSQL for persistent storage.
- Flyway for database migrations.
- Docker support for containerized deployment.

## Prerequisites
- Java 11
- Maven 3.x
- Docker (optional)
- PostgreSQL

## Setup

### 1. Clone the Repository
```bash
git clone https://github.com/your-repo/device-service.git
cd device-service