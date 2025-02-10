# Use a base image with Java 11
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/device-service-1.0-SNAPSHOT.jar /app/device-service.jar

# Copy the configuration file
COPY config.yml /app/config.yml

# Expose the port the application will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "device-service.jar", "server", "config.yml"]