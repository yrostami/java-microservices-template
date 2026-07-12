# Service Template

A comprehensive Spring Boot microservice template designed for rapid development and deployment of scalable applications within the Youtopin ecosystem.

## 🚀 Features

- **Multi-Module Architecture**: Parent-child structure for organized development
- **Modern Java**: Built with Java 21 and Spring Boot 3.5.6
- **Multi-Database Support**: PostgreSQL for relational data and MongoDB for NoSQL
- **Event-Driven Architecture**: Apache Kafka integration for asynchronous messaging
- **Observability**: OpenTelemetry integration for distributed tracing and metrics
- **API Documentation**: OpenAPI/Swagger UI for REST API documentation
- **Distributed Locking**: ShedLock for preventing duplicate scheduled tasks
- **Configuration Management**: Spring Cloud Config for externalized configuration
- **Container Ready**: Docker containerization with Jib Maven plugin
- **Code Quality**: Lombok for boilerplate reduction, MapStruct for object mapping
- **Testing**: Comprehensive test setup with Testcontainers

## 🏗️ Architecture

This template follows a microservice architecture with the following key components:

- **Spring Boot Web**: REST API development
- **Spring Data JPA**: Relational database operations with PostgreSQL
- **Spring Data MongoDB**: NoSQL document database operations
- **Spring Kafka**: Event-driven messaging
- **Spring Cloud OpenFeign**: Declarative HTTP clients
- **Spring Cloud Config**: External configuration management
- **Spring Boot Actuator**: Application monitoring and health checks

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker and Docker Compose (for containerized development)

## 🛠️ Technology Stack

### Core Framework
- **Spring Boot 3.5.6**: Main application framework
- **Spring Cloud 2025.0.0**: Microservice coordination

### Data Layer
- **PostgreSQL**: Primary relational database
- **MongoDB**: Document database
- **Flyway**: Database migration management
- **Spring Data JPA**: ORM for PostgreSQL
- **Spring Data MongoDB**: MongoDB integration

### Messaging
- **Apache Kafka**: Distributed event streaming platform
- **Spring Kafka**: Kafka integration for Spring applications

### Observability
- **OpenTelemetry**: Distributed tracing and observability
- **Micrometer**: Application metrics collection
- **Spring Boot Actuator**: Health checks and monitoring endpoints

### Development Tools
- **Lombok**: Boilerplate code reduction
- **MapStruct**: Object mapping
- **SpringDoc OpenAPI**: API documentation
- **ShedLock**: Distributed locking for scheduled tasks

### Deployment
- **Jib Maven Plugin**: Container image building
- **Eclipse Temurin 21 JRE**: Base container image

## 🏃 Getting Started

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ytp-service-template
   ```

2. **Build the application**
   ```bash
   ./mvnw clean compile
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run -P local
   ```

The application will start on `http://localhost:8080` with management endpoints on `http://localhost:8081`.

### Configuration

The application supports multiple environments:

- **local**: Development environment (default)
- **staging**: Staging environment
- **prod**: Production environment

Configuration is managed through:
- `application.properties`: Common configuration
- `application-{profile}.properties`: Environment-specific configuration
- External configuration server (for staging/prod)

## 🔧 Configuration

### Database Configuration

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/template_db
spring.datasource.username=template_user
spring.datasource.password=your_password

# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=template_db
```

### Kafka Configuration

```properties
# Consumer
spring.kafka.consumer.group-id=template
spring.kafka.consumer.isolation-level=read_committed

# Transactional Producer
spring.kafka.producer.acks=all
spring.kafka.producer.transaction-id-prefix=template-tx-
```

### Observability Configuration

```properties
# OpenTelemetry
otel.traces.exporter=otlp
otel.exporter.otlp.traces.endpoint=http://localhost:4318/v1/traces
otel.metrics.exporter=otlp
otel.exporter.otlp.metrics.endpoint=http://localhost:4318/v1/metrics
```

## 📁 Project Structure

```
ytp-service-template/
├── template/                          # Main application module
│   ├── src/main/java/com/company/template/
│   │   ├── Application.java           # Main Spring Boot application
│   │   ├── config/                    # Configuration classes
│   │   ├── controller/                # REST controllers
│   │   ├── service/                   # Business logic services
│   │   ├── repository/                # Data repositories
│   │   └── dto/                       # Data Transfer Objects
│   ├── src/main/resources/
│   │   ├── application.properties     # Main configuration
│   │   ├── application-local.properties
│   │   ├── application-staging.properties
│   │   ├── application-prod.properties
│   │   ├── kafka.properties           # Kafka configuration
│   │   ├── logback.xml               # Logging configuration
│   │   └── db/migration/             # Flyway migrations
│   └── src/test/                     # Test classes
├── template-spec/                    # Specification/Contract module
├── pom.xml                          # Parent POM
└── README.md                        # This file
```

## 🧪 Testing Infrastructure

### BaseIntegrationTest

The `BaseIntegrationTest` class provides a comprehensive foundation for integration testing with real dependencies:

**Key Features:**
- **WireMock Integration**: Automatic setup of WireMock container for mocking external API dependencies
- **Testcontainers Support**: Integration with PostgreSQL, MongoDB, and Kafka containers via `TestcontainersConfiguration`
- **Full Spring Context**: Complete application context with random port allocation
- **Dynamic Properties**: Automatic configuration of external service URLs for testing

**Use Cases:**
- **Controller Integration Testing**: Test REST endpoints with full request/response cycles
- **Database Integration**: Verify database operations with real PostgreSQL/MongoDB instances
- **External API Mocking**: Test interactions with third-party services using WireMock stubs
- **End-to-End Testing**: Validate complete workflows including Kafka message processing
- **Performance Testing**: Test application behavior under realistic conditions

**Example Usage:**
```java
public class SampleControllerIT extends BaseIntegrationTest {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCompleteWorkflow() throws Exception {
        // Test with real database and mocked external APIs
        SampleEntity sample = createTestSample();
        sampleRepository.save(sample);

        mockMvc.perform(get("/api/sample/{id}", sample.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name", equalTo(sample.getName())));
    }
}
```

### FeignClientTestBase

The `FeignClientTestBase` class provides a specialized foundation for testing Feign HTTP clients:

**Key Features:**
- **WireMock Integration**: Dedicated WireMock container for mocking external REST API responses
- **Feign Client Auto-Configuration**: Automatic setup of Feign client infrastructure and HTTP message converters
- **Selective Client Enabling**: Enables only Feign clients in the integration package (`com.company.**.integration.web`)
- **Spring Test Integration**: Uses SpringExtension for JUnit 5 compatibility
- **Property Loading**: Loads both application test properties and third-party API configurations

**Use Cases:**
- **Feign Client Testing**: Test HTTP client behavior with mocked external services
- **External API Integration**: Validate client-side error handling and response mapping
- **Contract Testing**: Verify client implementations against expected API contracts
- **Network Resilience Testing**: Test client behavior with various HTTP scenarios (timeouts, retries, error responses)
- **Request/Response Mapping**: Validate proper serialization and deserialization of API payloads

**Example Usage:**
```java
public class ExternalApiClientTest extends FeignClientTestBase {

    @Autowired
    private ExternalApiClient externalApiClient;

    @Test
    public void testExternalApiCall() {
        // WireMock automatically provides mocked responses
        ExternalApiResponse response = externalApiClient.callExternalService();

        assertNotNull(response);
        assertEquals("expected-value", response.getData());
    }
}
```

The base class handles:
- WireMock server setup and lifecycle management
- Feign client configuration and dependency injection
- Dynamic configuration of external service base URLs
- Loading of test-specific properties for API endpoints



The template includes ShedLock for preventing duplicate execution of scheduled tasks across multiple instances:

```java
@Scheduled(cron = "0 */5 * * * *")
@SchedulerLock(name = "sampleScheduledTask")
public void sampleScheduledTask() {
    // Task implementation
}
```

## 📊 Monitoring

### Health Checks
- Application health: `http://localhost:8081/actuator/health`
- Database connectivity: `http://localhost:8081/actuator/health/db`
- MongoDB connectivity: `http://localhost:8081/actuator/health/mongo`

### Metrics
- Application metrics: `http://localhost:8081/actuator/metrics`
- Custom business metrics available

### Tracing
Distributed tracing data is exported to OpenTelemetry collector for visualization and analysis.

## 🚢 Deployment

### Container Build
```bash
./mvnw compile jib:build
```

### Environment Variables
For production deployment, set the following environment variables:
- `SPRING_PROFILES_ACTIVE=prod`
- Database connection details
- Kafka broker information
- OpenTelemetry collector endpoints

## 🤝 Contributing

1. Follow the existing code style (enforced by Checkstyle)
2. Write tests for new functionality
3. Update documentation as needed
4. Ensure all tests pass before submitting PR

## 📝 Code Quality

- **Checkstyle**: Code style validation
- **Spring Java Format**: Consistent code formatting
- **Test Coverage**: Comprehensive test suite with Testcontainers

## 🔗 Related Services

This template is designed to work with:
- **Config Server**: External configuration management
- **API Gateway**: Request routing and filtering
- **Monitoring Stack**: Centralized logging and metrics collection

## 📄 License

This project is proprietary to Youtopin. All rights reserved.
