# gabsrell restaurant

Based on the software architecture specialization tech challenge,
this project is a restaurant management system.

## Modules

### Core
**Architecture:** Shared Kernel (DDD)  
**Technologies:** Java 21, Protocol Buffers (protobuf), gRPC, Jakarta Validation, Hibernate Validator  
**Description:** Shared module containing domain models, gRPC contracts (Protocol Buffers), and common validations used by all microservices. Defines Value Objects and shared entities across the system.

**Key Technologies:**
- Protocol Buffers 4.28.2 for API contract definition
- gRPC 1.63.0 for synchronous service-to-service communication
- Jakarta Validation for model validation
- Hibernate Validator for advanced validation

**Bazel Build:**
- **Dependencies:** Java 21 JDK, Bazel 8.3.1+
- **Build target:** `//core:artifact`
- **Test target:** `//core:unit`
- **No additional system dependencies required** - all dependencies are managed via Bazel's Maven integration

---

### Orders
**Architecture:** Domain-Driven Design (DDD) + Hexagonal Architecture (Ports & Adapters)  
**Framework:** Spring Boot 3.2.4  
**Language:** Java 21  
**Database:** MongoDB 6.0.6 (with Liquibase for migrations)  
**Cache:** Redis 7.0.13  
**Communication:** Kafka (event-driven), gRPC (synchronous)  
**Observability:** OpenTelemetry, New Relic APM  
**Testing:** JUnit 5, Mockito, Testcontainers, Cucumber (BDD), Specmatic (Contract Testing), Rest Assured

**Architectural Patterns:**
- **Hexagonal Architecture:** Clear separation between core (domain + application) and adapters (driven/driver)
- **DDD:** Aggregates, Value Objects, Domain Events, Use Cases
- **CQRS:** Separation between Commands and Queries
- **Event-Driven:** Event publishing via Kafka using CloudEvents

**Structure:**
- `core/domain`: Domain entities (Order, OrderItem), domain events, domain exceptions
- `core/application`: Use Cases, Commands, Queries
- `adapter/driver`: HTTP Controllers (REST), Kafka subscribers
- `adapter/driven`: MongoDB repositories, Kafka publishers, gRPC clients
- `infra`: Configurations (MongoDB, Kafka, Redis, Security, OpenTelemetry)

**Key Technologies:**
- Spring Boot Starter Web, Security, OAuth2 Resource Server
- Spring Kafka for asynchronous messaging
- MongoDB Driver Sync 5.0.1
- Liquibase 4.24.0 for MongoDB migrations
- Spring Data Redis for caching
- OpenAPI Generator 7.0.1 (code generation from YAML spec)
- CloudEvents 2.5.0 for standardized event format

**Testing:**
- **Unit:** Isolated tests with Mockito
- **Integration:** Tests with Testcontainers (MongoDB, Kafka)
- **Contract:** Contract tests with Specmatic
- **Behavior:** BDD tests with Cucumber and Gherkin
- **Security:** Spring Security Test for authorization validation

**TODO (from original design):** Migrate to full Event Sourcing with EventStoreDB + CQRS

**Bazel Build:**
- **Dependencies:** Java 21 JDK, Bazel 8.3.1+
- **Build target:** `//orders:artifact`
- **Test targets:** `//orders:unit`, `//orders:integration`, `//orders:contract`, `//orders:behavior`
- **Executable target:** `//orders:uber` (Spring Boot JAR)
- **Image target:** `//orders:image` (Docker image)
- **No additional system dependencies required** - all dependencies are managed via Bazel's Maven integration

---

### Menu
**Architecture:** Domain-Driven Design (DDD) + Hexagonal Architecture (Ports & Adapters)  
**Framework:** Quarkus 3.17.6  
**Language:** Java 21  
**Database:** MongoDB 6.0.6 (with Mongock for migrations)  
**Communication:** Kafka (event-driven), gRPC (synchronous)  
**Testing:** JUnit 5, Quarkus Test

**Architectural Patterns:**
- **Hexagonal Architecture:** Isolated core with ports for repositories and publishers
- **DDD:** Aggregates (Product, Ingredient), Domain Events, Use Cases
- **Event-Driven:** Event publishing for product/ingredient creation

**Structure:**
- `core/domain`: Domain models (Product, Ingredient, Menu), events, exceptions
- `core/application`: Use Cases (ProductUseCase, IngredientUseCase, MenuUseCase)
- `adapter/driver`: HTTP Controllers (REST), gRPC Controllers
- `adapter/driven`: MongoDB repositories, Kafka publishers
- `infra`: MongoDB configuration, Health Checks, Serializers

**Key Technologies:**
- Quarkus RESTEasy for REST APIs
- Quarkus MongoDB Client for persistence
- Mongock 5.4.1 for MongoDB migrations
- Quarkus Messaging Kafka for events
- Quarkus gRPC for synchronous communication
- Quarkus SmallRye Health for health checks
- OpenAPI Generator 7.0.1 (JAX-RS Spec generator)

**Characteristics:**
- Cloud-native optimized reactive framework
- Fast startup and low memory footprint
- Native image support with GraalVM

**Bazel Build:**
- **Dependencies:** Java 21 JDK, Bazel 8.3.1+
- **Build target:** `//menu:artifact`
- **Test target:** `//menu:unit`
- **Executable target:** `//menu:uber` (Quarkus JAR built with custom rules_quarkus)
- **Image target:** `//menu:image` (Docker image)
- **No additional system dependencies required** - all dependencies are managed via Bazel's Maven integration

---

### Permissions
**Architecture:** Domain-Driven Design (DDD) + MVC  
**Framework:** Spring Boot 3.2.4  
**Language:** Java 21  
**Database:** PostgreSQL 17.2 (with Liquibase for migrations)  
**Authentication/Authorization:** Auth0 (OAuth2/OIDC)  
**Communication:** gRPC (synchronous)  
**HTTP Client:** Unirest Java  
**Testing:** JUnit 5, Spring Security Test

**Architectural Patterns:**
- **MVC:** Clear separation between Controllers (UI), Services (Application), Repositories (Domain)
- **DDD:** Domain entities (Role, Authority, RoleAuthority)
- **Security:** Spring Security with OAuth2 Resource Server and Auth0 integration

**Structure:**
- `ui/controller`: HTTP REST and gRPC controllers
- `application/service`: Application services (PermissionService)
- `domain/model`: Domain entities (Role, Authority, RoleAuthority)
- `domain/repository`: Repository interfaces (JPA)
- `infraestructure/security`: Spring Security configuration
- `infraestructure/provider`: Auth0 integration

**Key Technologies:**
- Spring Boot Starter Web, Security, OAuth2 Resource Server
- Spring Data JPA for persistence
- PostgreSQL Driver 42.7.3
- Liquibase 4.27.0 for SQL migrations
- Auth0 Spring Security API 1.5.3
- gRPC Spring Boot Starter 3.1.0
- Unirest Java 4.3.1 for external HTTP calls

**Features:**
- Role (Group) and Authority (Scope) management
- Auth0 integration for authentication
- Permission validation via JWT tokens
- REST and gRPC APIs to expose permissions

**TODOs (from original design):** 
- Implement audit table for permission changes
- Integrate Kafka to emit permission-related events

**Bazel Build:**
- **Dependencies:** Java 21 JDK, Bazel 8.3.1+
- **Build target:** `//permissions:artifact`
- **Test targets:** `//permissions:unit`, `//permissions:integration`
- **Executable target:** `//permissions:uber` (Spring Boot JAR)
- **Image target:** `//permissions:image` (Docker image)
- **No additional system dependencies required** - all dependencies are managed via Bazel's Maven integration

---

### Customers
**Architecture:** Clean Architecture + Domain-Driven Design (DDD)  
**Framework:** FastAPI  
**Language:** Python 3  
**Database:** EdgeDB 3.5 (Graph-Relational Database)  
**Communication:** Kafka (event-driven)  
**DI:** Dependency Injector  
**Testing:** [To be defined]

**Architectural Patterns:**
- **Clean Architecture:** Separation into layers (Entities, Use Cases, Gateways, Controllers, Presenters)
- **DDD:** Domain entities (Customer)
- **Dependency Injection:** Explicit dependency injection container
- **Event-Driven:** Event publishing via Kafka using CloudEvents

**Structure:**
- `entities`: Domain entities (Customer)
- `use_cases`: Use cases (CustomerUseCase)
- `gateways`: Data access interfaces (CustomerGateway)
- `controllers`: HTTP controllers (CustomerController)
- `presenters`: Data presentation (CustomerPresenter)
- `db`: EdgeDB client and schema initialization
- `devices`: External devices (KafkaProducerDevice)
- `external_interfaces`: External event handlers
- `web`: FastAPI routes

**Key Technologies:**
- FastAPI for asynchronous REST APIs
- Uvicorn as ASGI server
- EdgeDB Python client for persistence
- kafka-python and aiokafka for asynchronous messaging
- Dependency Injector for dependency injection

**Characteristics:**
- Modern graph-relational database with EdgeDB
- Asynchronous APIs with FastAPI
- Schema migrations using EdgeDB migrations (ESDL)
- CloudEvents-based event publishing over Kafka

**TODO (from original Modules section):** Complete implementation of the Customers module

**Bazel Build:**
- **Dependencies:** Python 3.8+, Bazel 8.3.1+
- **Build target:** `//customers:artifact`
- **Executable target:** `//customers:uber` (Python binary with FastAPI)
- **System dependencies:** Python 3.8+ must be installed on the system (Bazel uses system Python)
- **Note:** EdgeDB migrations are handled at runtime, not during Bazel build

---

### Payments
**Architecture:** Domain-Driven Design (DDD) + Clean Architecture  
**Framework:** .NET  
**Language:** C#  
**Database:** Apache Cassandra  
**Payment Gateway:** Stripe  
**Communication:** Kafka (event-driven)  
**Status:** [TODO]

**Planned:**
- Integration with Stripe for payment processing
- Cassandra for distributed transaction storage
- Event-driven architecture for asynchronous processing

**Bazel Build:**
- **Dependencies:** .NET 8.0 SDK, Bazel 8.3.1+, Docker
- **Build target:** `//payments:artifact`
- **Test targets:** `//payments:unit`, `//payments:integration`
- **Executable target:** `//payments:uber` (Docker image with .NET runtime)
- **System dependencies:** .NET 8.0 SDK must be installed on the system for local builds
- **Note:** The build uses Dockerfile for .NET compilation, so Docker must be available
- Clean Architecture for clear separation of responsibilities

**TODO (from original Modules section):** Implement the Payments module with Stripe, Cassandra, Kafka and Clean Architecture

---

### Notifications
**Architecture:** Domain-Driven Design (DDD) + Service Layer  
**Framework:** Ruby on Rails 7.1 (API-only)  
**Language:** Ruby  
**Database:** PostgreSQL 17.2 (with ActiveRecord migrations)  
**Communication:** Kafka (event-driven, CloudEvents)  
**Job Processing:** ActiveJob (async)  
**Testing:** RSpec (planned)

**Architectural Patterns:**
- **Service Layer:** Clear separation between Controllers (API), Services (Business Logic), Jobs (Async Processing)
- **DDD:** Domain entities (Notification), Domain Events published to Kafka
- **Event-Driven:** Event publishing via Kafka using CloudEvents following DDD patterns
- **Generic Design:** Domain-agnostic service that can handle notifications for any entity type

**Structure:**
- `app/controllers`: REST API controllers (NotificationsController)
- `app/models`: Domain entities (Notification)
- `app/services`: Business logic (NotificationService, EmailNotificationSender, SmsNotificationSender, NotificationEventPublisher)
- `app/jobs`: Background jobs for asynchronous processing (SendNotificationJob)
- `config`: Rails configurations, Kafka initialization
- `db/migrate`: Database migrations

**Key Technologies:**
- Ruby on Rails 7.1 (API-only mode)
- PostgreSQL 17.2 for persistence
- ActiveRecord for ORM and migrations
- Apache Kafka for event publishing
- CloudEvents 1.0 for standardized event format
- ActiveJob for asynchronous job processing

**Features:**
- Generic notification service (no knowledge of other domains)
- Asynchronous notification processing via background jobs
- Support for Email and SMS notifications (mocked services)
- Event publishing to Kafka with DDD domain events:
  - `postech.notifications.v1.notification.sent`
  - `postech.notifications.v1.notification.failed`
- RESTful API for creating and querying notifications
- Generic entity support (entity_type, entity_id) instead of domain-specific fields

**API Endpoints:**
- `GET /api/v1/notifications` - List notifications (with pagination and filters)
- `GET /api/v1/notifications/:id` - Get notification details
- `POST /api/v1/notifications` - Create and enqueue notification (async)

**Environment Variables:**
- `POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`
- `KAFKA_SERVER_URL` - Kafka broker addresses
- `KAFKA_NOTIFICATIONS_TOPIC` - Topic for publishing notification events

**Characteristics:**
- Domain-agnostic design (generic entity_type/entity_id)
- Asynchronous processing for better scalability
- Event-driven architecture with CloudEvents
- No direct coupling with other microservices

**Bazel Build:**
- **Dependencies:** Ruby 3.2.0+, Bundler, Bazel 8.3.1+, Docker
- **Build target:** `//notifications:artifact`
- **Test target:** `//notifications:unit` (RSpec tests)
- **Executable target:** `//notifications:uber` (Docker image with Rails)
- **System dependencies:** Ruby 3.2.0+ and Bundler must be installed on the system for local builds
- **Note:** The build uses Docker for Rails runtime, so Docker must be available

---

### Infra
**Technologies:** Docker Compose, Kubernetes, Terraform, AWS Academy, Docker Hub, GitHub Actions  
**Status:** SAGA Pattern [TODO]

**Infrastructure Components:**
- **Docker Compose:** Local orchestration of services (PostgreSQL, MongoDB, EdgeDB, Redis, Kafka, Zookeeper, Cassandra, and all microservices)
- **Kubernetes:** Container orchestration in production
- **Terraform:** Infrastructure as Code for cloud provisioning
- **AWS Academy:** AWS learning environment
- **Docker Hub:** Docker image registry
- **GitHub Actions:** CI/CD pipelines
- **Bazel:** Build system for build and tests

**Orchestrated Services:**
- PostgreSQL 17.2 (Permissions, Notifications)
- MongoDB 6.0.6 (Orders, Menu)
- EdgeDB 3.5 (Customers)
- Redis 7.0.13 (Cache)
- Kafka 7.4.0 + Zookeeper (Event Streaming)
- Apache Cassandra 4.1 (Payments)

**TODO (from original Modules section):** Implement SAGA pattern for distributed transaction orchestration

## Global Architecture

Restaurant management system based on **Microservices** with asynchronous (event-driven) and synchronous (gRPC) communication. Each module follows **Domain-Driven Design (DDD)** principles and modern architectures (Hexagonal, Clean Architecture, MVC) according to its bounded context.

### Patterns and Practices

- **Event-Driven Architecture:** Asynchronous communication via Apache Kafka using CloudEvents
- **API-First:** Contracts defined in OpenAPI (Swagger) with code generation
- **Protocol Buffers:** gRPC contracts defined in `.proto` files for synchronous communication
- **Test-Driven Development:** Multiple testing layers (Unit, Integration, Contract, Behavior)
- **Infrastructure as Code:** Terraform for provisioning, Docker Compose for local development
- **CI/CD:** GitHub Actions pipelines for build, test, and deploy
- **Observability:** OpenTelemetry for distributed tracing, New Relic for APM
- **Security:** OAuth2/OIDC with Auth0, Spring Security for role-based authorization

### Main Technology Stack

**Backend:**
- Java 21 (Orders, Menu, Permissions, Core)
- Python 3 (Customers)
- Ruby (Notifications)
- .NET (Payments - planned)

**Frameworks:**
- Spring Boot 3.2.4
- Quarkus 3.17.6
- FastAPI
- Ruby on Rails 7.1

**Databases:**
- MongoDB 6.0.6 (Orders, Menu)
- PostgreSQL 17.2 (Permissions, Notifications)
- EdgeDB 3.5 (Customers)
- Redis 7.0.13 (Cache)
- Apache Cassandra 4.1 (Payments)

**Messaging:**
- Apache Kafka 7.4.0
- CloudEvents 2.5.0

**Communication:**
- gRPC 1.63.0 (synchronous)
- REST APIs (HTTP/JSON)
- Protocol Buffers 4.28.2

**Build & Deploy:**
- Bazel (build system)
- Maven (dependency management)
- Docker & Docker Compose
- Kubernetes
- Terraform

**Testing:**
- JUnit 5
- Mockito
- Testcontainers
- Cucumber (BDD)
- Specmatic (Contract Testing)
- Rest Assured

**Observability:**
- OpenTelemetry
- New Relic APM

## Docs

https://miro.com/app/board/uXjVNf1J6J8=/?share_link_id=738234968069

## Bazel

```
    bazel clean --expunge
    
    bazel build //core:artifact
    bazel test //core:unit
    
    bazel build //permissions:artifact
    bazel build //permissions:uber
    bazel build //permissions:image
    bazel run //permissions:push
    
    bazel build //orders:artifact
    bazel test //orders:unit
    bazel test //orders:integration
    bazel test //orders:contract
    bazel test //orders:behavior
    bazel build //orders:uber
    bazel build //orders:image
    bazel run //orders:push
    
    bazel build //menu:artifact
    bazel test //menu:unit
    bazel build //menu:uber
    bazel build //menu:image
    bazel run //menu:push
    
    bazel build //notifications:notifications_build
    bazel test //notifications:unit
    bazel build //notifications:notifications_image
    bazel run //notifications:notifications_push
```