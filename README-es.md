# Proyecto: Spring Boot con Docker Compose y PostgreSQL usando Docker Socket

Este proyecto es un ejemplo de una aplicación Spring Boot que gestiona un servicio de base de datos PostgreSQL utilizando Docker Compose y el socket de Docker (`/var/run/docker.sock`). Incluye un `Dockerfile` y un archivo `docker-compose.yml` para la fácil implementación de la aplicación y base de datos.

## 📦 Estructura del Proyecto

```plaintext
src
├── main
│   ├── java/top/anyel/sock
│   │   ├── DockerController.java
│   │   └── utils/DockerEnvironmentUtil.java
│   ├── resources
│   │   └── docker
│   │       ├── docker-compose.yml
│   │       └── .env (generado al configurar)
├── target
│   └── sock-0.0.1-SNAPSHOT.jar
├── Dockerfile
└── README.md
```

---

## 🚀 Requisitos Previos
- Docker instalado
- Java 17
- Maven

---

## 🛠️ Configuración y Ejecución

### 1. Construcción del JAR
```bash
mvn clean package
```

### 2. Construcción de la Imagen Docker
```bash
docker build -t springboot-docker-sock:latest .
```

### 3. Ejecutar el Contenedor con Docker Socket
```bash
docker run -d \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -p 8080:8080 \
  --name springboot-docker-sock \
  springboot-docker-sock:latest
```

---

## 📡 Endpoints de la API

### **Configurar Variables de Entorno (Envía al socket de Docker)**
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

### **Ejecutar Docker Compose (Ejecuta usando Docker Socket)**
```http
POST /api/docker/ejecutar
```
Este endpoint levanta los servicios de PostgreSQL usando las variables definidas y comunicándose con el daemon de Docker del host a través de `/var/run/docker.sock`.

---

## 📄 Dockerfile
```dockerfile
# Base image with JDK 17
FROM openjdk:17-jdk-alpine

# Instalar Docker CLI y Docker Compose
RUN apk add --no-cache docker-cli docker-compose

# Set the working directory
WORKDIR /app

# Copy JAR and Docker files
COPY target/sock-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/docker/ docker/

# Expose port
EXPOSE 8080

# Habilitar Docker Socket para ejecutar comandos en el host
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

## 📦 Verificar que PostgreSQL esté Corriendo
Para verificar que el contenedor está activo y ejecutándose en el host usando Docker Socket:
```bash
docker ps
```

Para conectarse al contenedor de PostgreSQL y validar la base de datos:
```bash
docker exec -it <container_id> psql -U admin -d testdb
```

---

## 🛡️ Seguridad y Uso del Docker Socket
- **Docker Socket (`/var/run/docker.sock`)** se usa para permitir que el contenedor interactúe con el daemon de Docker en la máquina anfitriona.
- Las credenciales son gestionadas a través de un `POST` al endpoint `/api/docker/configurar` y no se almacenan de forma permanente en archivos.

---

## 📧 Autor
**Anyel EC**
- GitHub: [Anyel-ec](https://github.com/Anyel-ec)


