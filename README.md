# Authentication Service

## Overview

A production-ready, stateless JWT-based authentication service built with Spring Boot. This service provides secure user authentication, registration, and role-based access control (RBAC) with comprehensive exception handling and security best practices.

## Technology Stack

* **Java**: 21
* **Spring Boot**: 3.5.8
* **Spring Security**: 6.5.7
* **Authentication**: Stateless JWT (JSON Web Tokens)
* **Database**: MySQL 8.0+
* **JWT Library**: jsonwebtoken 0.11.5
* **Build Tool**: Maven 3.9+
* **Mapping**: MapStruct 1.6.3
* **Lombok**: For boilerplate reduction
* **Testing**: JUnit 5 with Spring Boot Test

## Features

* ✅ User registration and login with stateless JWT tokens
* ✅ Custom `AuthenticationProvider` for authentication logic
* ✅ `SecurityFilterChain` for fine-grained HTTP security configuration
* ✅ JWT token validation in `JwtAuthenticationFilter.doFilterInternal()`
* ✅ Role-based access control (USER / ADMIN)
* ✅ Global exception handling with `@RestControllerAdvice`
* ✅ Clean architecture: thin controllers, service layer separation
* ✅ Password encoding with BCrypt
* ✅ Integration tests for authentication flow
* ✅ Spring Actuator for health monitoring
* ✅ Docker support with multi-stage builds

## Prerequisites

* Java 21 or higher
* Maven 3.9+ 
* MySQL 8.0+ (or Docker for running MySQL)
* Docker (optional, for containerized deployment)

## Quick Start

### 1. Clone and Build

```bash
git clone <repository-url>
cd auth-service
mvn clean install
```

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE jwt_auth;
```

### 3. Configuration

Configure your database connection in `src/main/resources/application-dev.properties` or set environment variables:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jwt_auth
spring.datasource.username=root
spring.datasource.password=your_password
```

**Important**: Update the JWT secret key in `application.properties` or set it via environment variable:

```bash
export SECURITY_JWT_SECRET_KEY=your-256-bit-secret-key-here
```

### 4. Run the Application

```bash
# Using Maven
mvn spring-boot:run

# Or using the packaged JAR
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

The application will start on port **8085** (default) and will be available at `http://localhost:8085`

## API Endpoints

### Base URL
```
http://localhost:8085/api
```

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "roles": ["USER"]
}
```

**Response** (201 Created):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "securePassword123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Protected Endpoints

#### Get All Users (Admin Only)
```http
GET /api/secure
Authorization: Bearer <your-jwt-token>
```

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "roles": ["USER"]
  }
]
```

**Note**: This endpoint requires the `ADMIN` role.

### Health Check

```http
GET /actuator/health
```

## Configuration

### Application Properties

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Server port | 8085 |
| `spring.profiles.active` | Active profile | dev |
| `security.jwt.secret-key` | JWT signing key | (must be set) |
| `security.jwt.expiration-time` | Token expiration (ms) | 3600000 (1 hour) |

### Profiles

The application supports multiple profiles:

- **dev**: Development profile with H2/MySQL, data initialization enabled
- **test**: Test profile with in-memory H2 database
- **docker**: Production profile with environment variable configuration

### Environment Variables

For production, use environment variables:

```bash
export ACTIVE_PROFILE=docker
export DATABASE_URL=jdbc:mysql://mysql:3306/jwt_auth
export DATABASE_USERNAME=root
export DATABASE_PASSWORD=your_password
export SECURITY_JWT_SECRET_KEY=your-secret-key
export SECURITY_JWT_EXPIRATION_TIME=3600000
```

## Bootstrap Data

The application automatically creates default roles and users at startup (dev profile only). This is implemented in `com.khouloud.auth.config.DataInitializer`.

### Default Roles
- `USER`
- `ADMIN`

### Default Users

| Username | Full Name | Roles | Password |
|----------|-----------|-------|----------|
| user@email | Default User | USER | change-me |
| admin@email | System Administrator | ADMIN, USER | change-me |

**⚠️ Security Warning**: Change these default passwords in production!

## Docker Deployment

### Build Docker Image

```bash
docker build -t auth-service:latest -f dockerfile .
```

### Run with Docker

```bash
docker run -d \
  -p 8080:8080 \
  -e ACTIVE_PROFILE=docker \
  -e DATABASE_URL=jdbc:mysql://host.docker.internal:3306/jwt_auth \
  -e DATABASE_USERNAME=root \
  -e DATABASE_PASSWORD=your_password \
  -e SECURITY_JWT_SECRET_KEY=your-secret-key \
  --name auth-service \
  auth-service:latest
```

### Docker Compose Example

Create a `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: jwt_auth
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  auth-service:
    build:
      context: .
      dockerfile: dockerfile
    ports:
      - "8080:8080"
    environment:
      ACTIVE_PROFILE: docker
      DATABASE_URL: jdbc:mysql://mysql:3306/jwt_auth
      DATABASE_USERNAME: root
      DATABASE_PASSWORD: rootpassword
      SECURITY_JWT_SECRET_KEY: your-256-bit-secret-key-here
    depends_on:
      - mysql
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 3s
      retries: 3
      start_period: 40s

volumes:
  mysql_data:
```

Run with:
```bash
docker-compose up -d
```

## Testing

### Run Tests

```bash
# Run all tests
mvn test

# Run integration tests only
mvn test -Dtest=*IntegrationTest
```

### Test Coverage

This project focuses on integration testing to validate:
- JWT authentication flow
- Spring Security filter chain
- Protected vs public endpoints
- Real HTTP request/response behavior


## Security Considerations

1. **JWT Secret Key**: Always use a strong, randomly generated secret key (minimum 256 bits) in production
2. **HTTPS**: Use HTTPS in production to protect tokens in transit
3. **Token Expiration**: Tokens expire after 1 hour by default
4. **Password Security**: Passwords are hashed using BCrypt
5. **Role-Based Access**: Implement proper role checks for sensitive endpoints
6. **Input Validation**: All inputs are validated using Jakarta Validation

## Project Structure

```
src/
├── main/
│   ├── java/com/khouloud/auth/
│   │   ├── api/              # REST Controllers
│   │   ├── config/           # Configuration classes
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── mapper/           # MapStruct mappers
│   │   ├── model/            # Entity models
│   │   ├── repository/       # JPA repositories
│   │   ├── security/         # Security configuration
│   │   └── service/          # Business logic
│   └── resources/
│       ├── application*.properties
└── test/
    └── java/com/khouloud/auth/
        └── integration/      # Integration tests
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request


## Support

For issues and questions, please open an issue in the repository.
