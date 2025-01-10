# Project: Spring Boot with Docker Compose and PostgreSQL using Docker Socket

This project is an example of a Spring Boot application that manages a PostgreSQL database service using Docker Compose and the Docker socket (`/var/run/docker.sock`). It includes a `Dockerfile` and a `docker-compose.yml` file for easy deployment of the application and database.

## 📦 Project Structure

```plaintext
src
├── main
│   ├── java/top/anyel/sock
│   │   ├── DockerController.java
│   │   └── utils/DockerEnvironmentUtil.java
│   ├── resources
│   │   └── docker
│   │       ├── docker-compose.yml
│   │       └── .env (generated on configuration)
├── target
│   └── sock-0.0.1-SNAPSHOT.jar
├── Dockerfile
└── README.md
```

---

## 🚀 Prerequisites
- Docker installed
- Java 17
- Maven

---

## 🛠️ Setup and Execution

### 1. Build the JAR
```bash
mvn clean package
```

### 2. Build the Docker Image
```bash
docker build -t springboot-docker-sock:latest .
```

### 3. Run the Container with Docker Socket
```bash
docker run -d \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -p 8080:8080 \
  --name springboot-docker-sock \
  springboot-docker-sock:latest
```

---

## 📡 API Endpoints

### **Configure Environment Variables (Sent to Docker Socket)**
```http
POST /api/docker/configurar
```
**Body (JSON):**
```json
{
  "POSTGRES_DB": "testdb",
  "POSTGRES_USER": "admin",
  "POSTGRES_PASSWORD": "password",
  "POSTGRES_PORT_HOST": "5432",
  "POSTGRES_PORT_CONTAINER": "5432"
}
```

### **Run Docker Compose (Using Docker Socket)**
```http
POST /api/docker/ejecutar
```
This endpoint launches PostgreSQL services using the provided variables and communicates with the Docker daemon on the host via `/var/run/docker.sock`.

---

## 📄 Dockerfile
```dockerfile
# Base image with JDK 17
FROM openjdk:17-jdk-alpine

# Install Docker CLI and Docker Compose
RUN apk add --no-cache docker-cli docker-compose

# Set the working directory
WORKDIR /app

# Copy JAR and Docker files
COPY target/sock-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/docker/ docker/

# Expose port
EXPOSE 8080

# Enable Docker Socket to run commands on the host
VOLUME ["/var/run/docker.sock"]

# Run the backend
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🐘 docker-compose.yml
```yaml
version: '3'

services:
  postgresql_db:
    image: postgres:latest
    volumes:
      - postgres_data:/var/lib/postgresql/data
    environment:
      POSTGRES_DB: ${POSTGRES_DB:-defaultdb}
      POSTGRES_USER: ${POSTGRES_USER:-defaultuser}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-defaultpassword}
    ports:
      - ${POSTGRES_PORT_HOST:-5432}:${POSTGRES_PORT_CONTAINER:-5432}

volumes:
  postgres_data:
```

---

## 📦 Verify PostgreSQL is Running
To verify that the container is active and running on the host using Docker Socket:
```bash
docker ps
```

To connect to the PostgreSQL container and validate the database:
```bash
docker exec -it <container_id> psql -U admin -d testdb
```

---

## 🛡️ Security and Usage of Docker Socket
- **Docker Socket (`/var/run/docker.sock`)** allows the container to interact with the Docker daemon on the host machine.
- Credentials are managed via a `POST` request to the `/api/docker/configurar` endpoint and are not permanently stored in files.

---

## 📧 Author
**Anyel EC**
- GitHub: [Anyel-ec](https://github.com/Anyel-ec)

