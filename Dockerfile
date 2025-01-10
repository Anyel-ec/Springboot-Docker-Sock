# Base image with JDK 17
FROM openjdk:17-jdk-alpine

# Instalar Docker CLI y Docker Compose (V1 clásico)
RUN apk add --no-cache docker-cli docker-compose

# Set the working directory
WORKDIR /app

# Copy the JAR file and the Docker Compose files
COPY target/sock-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/docker/ docker/

# Ensure the application can execute Docker Compose on the host
VOLUME ["/var/run/docker.sock"]

# Expose the port
EXPOSE 8080

# Run the backend
ENTRYPOINT ["java", "-jar", "app.jar"]
