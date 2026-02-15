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
- **Dependencies:** Bazel 8.3.1+ (Java 21 JDK is automatically managed via hermetic toolchain)
- **Build target:** `//core:artifact`
- **Test target:** `//core:unit`
- **No system dependencies required** - all dependencies are managed via Bazel's Maven integration and hermetic toolchains

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
- **Dependencies:** Bazel 8.3.1+ (Java 21 JDK is automatically managed via hermetic toolchain)
- **Build target:** `//orders:artifact`
- **Test targets:** `//orders:unit`, `//orders:integration`, `//orders:contract`, `//orders:behavior`
- **Executable target:** `//orders:uber` (Spring Boot JAR - no Docker required)
- **Image target:** `//orders:image` (Docker image - Docker required only for this target)
- **No system dependencies required** - all dependencies are managed via Bazel's Maven integration and hermetic toolchains

---

### Menu
**Architecture:** Domain-Driven Design (DDD) + Hexagonal Architecture (Ports & Adapters)  
**Framework:** Gin 1.10  
**Language:** Go 1.23  
**Database:** MongoDB (official driver `go.mongodb.org/mongo-driver`)  
**Communication:** Kafka (event-driven; publisher currently no-op, pluggable)  
**Testing:** Go testing package, `go test`

**Architectural Patterns:**
- **Hexagonal Architecture:** Isolated core with ports for repositories and publishers
- **DDD:** Aggregates (Product, Ingredient), Domain Events, Use Cases
- **Event-Driven:** Event publishing for product/ingredient creation (publisher interface; Kafka implementation optional)

**Structure:**
- `internal/domain`: Domain models (Product, Ingredient, Category), errors
- `internal/application`: Use cases (ProductService, IngredientService), DTOs, ports (interfaces)
- `internal/adapter/http`: Gin handlers (products, ingredients), router
- `internal/adapter/repository`: MongoDB repositories (products, ingredients)
- `internal/adapter/messaging`: Publishers (no-op; can be replaced by Kafka)
- `internal/config`: Configuration from environment
- `cmd/server`: Application entrypoint

**Key Technologies:**
- Gin 1.10 for REST APIs (routing, JSON, validation)
- go.mongodb.org/mongo-driver 1.17 for persistence
- google/uuid for ID generation
- Standard library `context`, `net/http`

**Characteristics:**
- Single static binary, fast startup and low memory
- No JVM; same target names as other services (artifact, uber, unit, integration, layer, image, push) for pipeline consistency

**Bazel Build:**
- **Dependencies:** Bazel 8.3.1+ (Go 1.23.4 SDK is automatically managed via `rules_go` hermetic toolchain)
- **Build target:** `//menu:artifact`
- **Test targets:** `//menu:unit`, `//menu:integration`
- **Executable target:** `//menu:uber` (Go binary - no Docker required)
- **Image target:** `//menu:image` (Docker image - Docker required only for this target)
- **No system dependencies required** - Go SDK and dependencies are managed via Bazel's `rules_go` and Gazelle `go_deps`

---

### Permissions
**Architecture:** Domain-Driven Design (DDD) + MVC
**Framework:** Spring Boot 3.2.4
**Languages:** Kotlin 2.1.0 (domain models) + Java 21 (application layer)
**Database:** PostgreSQL 17.2 (with Liquibase for migrations)
**Authentication/Authorization:** Auth0 (OAuth2/OIDC)
**Communication:** gRPC (synchronous)
**HTTP Client:** Unirest Java
**Testing:** JUnit 5, Spring Security Test

**Architectural Patterns:**
- **MVC:** Clear separation between Controllers (UI), Services (Application), Repositories (Domain)
- **DDD:** Domain entities (Role, Authority, RoleAuthority) - **implemented in Kotlin**
- **Security:** Spring Security with OAuth2 Resource Server and Auth0 integration
- **Polyglot JVM:** Demonstrates Kotlin-Java interoperability within a single microservice

**Structure:**
- `domain/model` (**Kotlin**): Domain entities (Role, Authority, RoleAuthority), converters
- `ui/controller` (Java): HTTP REST and gRPC controllers
- `application/service` (Java): Application services (PermissionService)
- `domain/repository` (Java): Repository interfaces (JPA)
- `infraestructure/security` (Java): Spring Security configuration
- `infraestructure/provider` (Java): Auth0 integration

**Key Technologies:**
- Kotlin 2.1.0 with Spring Boot support
- Kotlin data classes for concise JPA entities
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
- **Dependencies:** Bazel 8.3.1+ (Java 21 JDK is automatically managed via hermetic toolchain)
- **Build target:** `//permissions:artifact`
- **Test targets:** `//permissions:unit`, `//permissions:integration`
- **Executable target:** `//permissions:uber` (Spring Boot JAR - no Docker required)
- **Image target:** `//permissions:image` (Docker image - Docker required only for this target)
- **No system dependencies required** - all dependencies are managed via Bazel's Maven integration and hermetic toolchains

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
- **Dependencies:** Bazel 8.3.1+ (Python 3.11 is automatically managed via hermetic toolchain)
- **Build target:** `//customers:artifact`
- **Executable target:** `//customers:uber` (Python binary - no Docker required)
- **No system dependencies required** - Python runtime is automatically downloaded and managed by Bazel
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
- **Dependencies:** Bazel 8.3.1+, Docker (required for uber target)
- **Build target:** `//payments:artifact`
- **Test targets:** `//payments:unit`, `//payments:integration`
- **Executable target:** `//payments:uber` (Docker image with .NET runtime - Docker required)
- **No system dependencies required** - .NET 10.0 SDK is automatically managed via hermetic toolchain
- **Note:** Docker is required because the uber target generates a Docker image (oci_image)
- Clean Architecture for clear separation of responsibilities

**TODO (from original Modules section):** Implement the Payments module with Stripe, Cassandra, Kafka and Clean Architecture

---

### Supplies
**Architecture:** Domain-Driven Design (DDD) + Layered Architecture
**Framework:** NestJS 10.3.0
**Language:** TypeScript 5.9.3
**Database:** [To be defined]
**Communication:** REST APIs
**Testing:** Jest (planned)

**Architectural Patterns:**
- **Layered Architecture:** Controllers, Services, Repositories pattern
- **DDD:** Domain entities for Supply management
- **Dependency Injection:** NestJS built-in DI container
- **API-First:** Swagger/OpenAPI documentation auto-generated

**Structure:**
- `src/`: TypeScript source files
- Controllers, Services, DTOs following NestJS patterns

**Key Technologies:**
- NestJS 10.3.0 for modern Node.js framework
- TypeScript 5.9.3 for type safety
- Class Validator and Class Transformer for validation
- Swagger for API documentation
- RxJS for reactive programming

**Characteristics:**
- Modern TypeScript/Node.js microservice
- Auto-generated API documentation
- Modular architecture with NestJS modules
- Type-safe development with TypeScript

**Bazel Build:**
- **Dependencies:** Bazel 8.3.1+, pnpm 10.29.3 (managed hermetically)
- **Build target:** `//supplies:artifact`
- **Executable target:** `//supplies:uber` (Node.js binary - no Docker required)
- **Image target:** `//supplies:image` (marked as manual due to pnpm symlink issues)
- **No system dependencies required** - Node.js 20.x and pnpm are automatically managed via hermetic toolchain
- **Note:** OCI image targets are marked as `manual` and excluded from CI/CD due to symlink issues with pnpm node_modules

---

### Deliveries
**Architecture:** Domain-Driven Design (DDD) + Hexagonal Architecture
**Framework:** Axum 0.7
**Language:** Rust (Edition 2021)
**Database:** [To be defined]
**Communication:** REST APIs (async)
**Testing:** [To be defined]

**Architectural Patterns:**
- **Hexagonal Architecture:** Handlers (driver) and Models (domain)
- **Async/Await:** Tokio runtime for async operations
- **Type Safety:** Rust's strong type system for compile-time guarantees

**Structure:**
- `src/lib.rs`: Library crate with domain modules
- `src/main.rs`: Binary entry point with Axum server
- `src/handlers.rs`: HTTP request handlers
- `src/models.rs`: Domain models and DTOs

**Key Technologies:**
- Axum 0.7 for fast, ergonomic web framework
- Tokio 1.0 for async runtime
- Serde for JSON serialization/deserialization
- Tower 0.4 for middleware
- Tracing for structured logging

**Characteristics:**
- High-performance Rust microservice
- Memory-safe with zero-cost abstractions
- Async HTTP server with Axum
- Compile-time guarantees for correctness

**Bazel Build:**
- **Dependencies:** Bazel 8.3.1+, Cargo (managed hermetically)
- **Build target:** `//deliveries:artifact` (library)
- **Executable target:** `//deliveries:uber` (Rust binary - no Docker required)
- **Image target:** `//deliveries:image` (Docker image)
- **No system dependencies required** - Rust toolchain is automatically managed via hermetic `rules_rust` v0.63.0
- **Cargo.lock:** Must be kept in sync with `Cargo.toml` - regenerate with `cargo generate-lockfile` if dependencies change

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
- **Dependencies:** Bazel 8.3.1+, Docker (required for uber target)
- **Build target:** `//notifications:artifact`
- **Test target:** `//notifications:unit` (RSpec tests)
- **Executable target:** `//notifications:uber` (Docker image with Rails - Docker required)
- **No system dependencies required** - Ruby 3.2.0 and all gems are automatically managed via hermetic toolchain
- **Hermetic gem management:** All Ruby gems are fetched hermetically via `bundle_fetch` with SHA256 checksums in `MODULE.bazel`
- **Gemfile.lock:** Must be kept in sync with `Gemfile` - regenerate with `bundle install` if dependencies change
- **Note:** Docker is required because the uber target generates a Docker image (oci_image)

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

### Polyglot Architecture Strategy

This project demonstrates a **polyglot microservices architecture** where each service uses the most appropriate language and technology stack for its domain:

**Current Language Distribution:**
- **Java 21** - 1 service (Orders) + Core shared module
- **Go 1.23** - 1 service (Menu)
- **Kotlin 2.1.0** - 1 service (Permissions)
- **TypeScript** - 1 service (Supplies)
- **Rust** - 1 service (Deliveries)
- **Python** - 1 service (Customers)
- **Ruby** - 1 service (Notifications)
- **C#/.NET** - 1 service (Payments)

**✅ Completed Migration: Permissions to Kotlin**

The Permissions service has been successfully migrated to use **Kotlin for domain models** while keeping Java for application logic:

**Migration Status:**
- ✅ **Domain entities:** Role, Authority, RoleAuthority - migrated to Kotlin data classes
- ✅ **Converters:** PermissionIDConverter, InstantAttributeConverter - migrated to Kotlin
- ⏸️ **Services, Controllers, Repositories:** Remaining in Java (seamless interoperability)

**Why Permissions was chosen:**

**Why Permissions?**
- **Smallest codebase:** 29 Java files (vs 54 in Menu, 117 in Orders)
- **Simplest domain:** Basic CRUD for Role/Authority/RoleAuthority entities
- **No complex patterns:** No event sourcing, CQRS, or distributed transactions
- **Single database:** PostgreSQL with JPA - Kotlin has excellent Spring Data support
- **Straightforward migration:** Java and Kotlin can coexist during migration

**Why Kotlin (not Scala)?**
- **100% Spring Boot compatibility** - Kotlin is officially supported by Spring
- **Gradual migration** - Can migrate file-by-file, Java and Kotlin interoperate seamlessly
- **Modern syntax** - Data classes, null safety, extension functions, coroutines
- **Same JVM** - No runtime overhead, same deployment model
- **Mature tooling** - `rules_kotlin` integrates with Bazel's `rules_jvm`
- **Lower learning curve** - More similar to Java than Scala

**Expected Benefits:**
- **Conciseness:** Kotlin reduces boilerplate (data classes, no semicolons, type inference)
- **Safety:** Null-safety at compile time prevents NullPointerExceptions
- **Modern features:** Extension functions, sealed classes, scope functions
- **Same performance:** JVM bytecode identical to Java

**Example Migration:**
```java
// Java
public class Role {
    private UUID id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    // Getters, setters, equals, hashCode, toString...
}
```

```kotlin
// Kotlin
data class Role(
    val id: UUID,
    val name: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

**Alternative Services to Migrate:**
- **Menu** - Migrated to **Go 1.23 + Gin** (no longer JVM)
- **Orders** (117 files) - Not recommended due to complexity and extensive test suites

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

**Backend Languages:**
- Java 21 (Orders, Core)
- Go 1.23 (Menu)
- Kotlin 2.1.0 (Permissions - domain layer)
- Java 21 + Kotlin 2.1.0 (Permissions - hybrid polyglot service)
- TypeScript 5.9.3 (Supplies)
- Rust (Edition 2021) (Deliveries)
- Python 3.11 (Customers)
- Ruby 3.2.0 (Notifications)
- C# / .NET 10.0 (Payments - planned)

**Frameworks:**
- Spring Boot 3.2.4 (Orders, Permissions)
- Gin 1.10 (Menu)
- NestJS 10.3.0 (Supplies)
- Axum 0.7 (Deliveries)
- FastAPI (Customers)
- Ruby on Rails 7.1 (Notifications)

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
- JUnit 5, Mockito, Testcontainers (Java/Kotlin)
- Go testing package (Menu)
- Cucumber (BDD)
- Specmatic (Contract Testing)
- Rest Assured

**Observability:**
- OpenTelemetry
- New Relic APM

## Docs

https://miro.com/app/board/uXjVNf1J6J8=/?share_link_id=738234968069

## Bazel

### Hermetic Builds (No Language Dependencies Required)

**All projects can be built on a raw machine without installing language-specific dependencies!**

The Bazel build system uses hermetic toolchains that automatically download and manage all required language runtimes:
- **Java 21** - Managed via `rules_java` and `contrib_rules_jvm`
- **Go 1.23.4** - Managed via `rules_go` v0.60.0 with hermetic Go SDK and Gazelle `go_deps` for module dependencies
- **Kotlin 2.1.0** - Managed via `rules_kotlin` v2.1.0 with Kotlin stdlib and reflect
- **TypeScript 5.9.3 / Node.js 20.x** - Managed via `aspect_rules_ts` and `aspect_rules_js` with pnpm 10.29.3
- **Rust (Edition 2021)** - Managed via `rules_rust` v0.63.0 with hermetic Rust toolchain and Cargo
- **Python 3.11** - Managed via `rules_python` with hermetic Python toolchain
- **Ruby 3.2.0** - Managed via `rules_ruby` with hermetic Ruby toolchain and `bundle_fetch` for gem dependencies
- **.NET 10.0** - Managed via `rules_dotnet` with hermetic .NET SDK

**Prerequisites:**
- **Bazel 8.3.1+** - The only system dependency required for most builds
- **Docker** - Only needed for:
  - Building `//notifications:uber` and `//payments:uber` (these generate Docker images)
  - Building `:image` targets (Docker images for all services)

**No need to install:**
- ❌ Java JDK
- ❌ Go toolchain
- ❌ Kotlin compiler
- ❌ Node.js / npm / pnpm
- ❌ Rust / Cargo
- ❌ Python interpreter
- ❌ Ruby interpreter
- ❌ .NET SDK
- ❌ Any language-specific package managers

All language runtimes are automatically downloaded and managed by Bazel's hermetic toolchains, ensuring reproducible builds across different machines.

**Language-Specific Lockfile Management:**

**Ruby (Notifications):**
- Gems are fetched hermetically using `bundle_fetch` with SHA256 checksums
- Checksums are defined in `MODULE.bazel` for reproducibility
- `Gemfile.lock` must be kept in sync with `Gemfile` - if you update `Gemfile`, regenerate `Gemfile.lock` using Docker:
  ```bash
  docker run --rm -v "$(pwd)/notifications:/app" -w /app ruby:3.2.0-slim \
    bash -c "apt-get update -qq && apt-get install -y -qq build-essential libpq-dev libffi-dev libyaml-dev libreadline-dev zlib1g-dev libssl-dev > /dev/null 2>&1 && gem install bundler --no-document && bundle install"
  ```

**TypeScript (Supplies):**
- Dependencies are managed via pnpm with `npm_translate_lock`
- `pnpm-lock.yaml` must be kept in sync with `package.json` - if you update `package.json`, regenerate with:
  ```bash
  cd supplies && pnpm install
  ```
- `pnpm-workspace.yaml` configures build scripts for specific packages

**Rust (Deliveries):**
- Crates are managed via Cargo with `crate_universe`
- `Cargo.lock` must be kept in sync with `Cargo.toml` - if you update `Cargo.toml`, regenerate with:
  ```bash
  cd deliveries && cargo generate-lockfile
  ```

**Go (Menu):**
- Dependencies are resolved from `menu/go.mod` via Gazelle `go_deps.from_file`
- `menu/go.sum` must be kept in sync with `menu/go.mod` - if you add or change dependencies, run:
  ```bash
  cd menu && go mod tidy
  ```
- Bazel uses hermetic Go SDK 1.23.4 (`rules_go`); no system Go required

**Docker Requirements:**
- ✅ **Not required** for: `//orders:uber`, `//permissions:uber`, `//menu:uber` (JARs or Go binary), `//customers:uber` (Python binary)
- ✅ **Required** for: `//notifications:uber`, `//payments:uber` (these generate Docker images)
- ✅ **Required** for: all `:image` targets (Docker images for deployment)

### Build Commands

```bash
# Clean build cache
bazel clean --expunge

# Core module
bazel build //core:artifact
bazel test //core:unit

# Permissions module
bazel build //permissions:artifact
bazel build //permissions:uber

# Orders module
bazel build //orders:artifact
bazel test //orders:unit
bazel test //orders:integration
bazel test //orders:contract
bazel test //orders:behavior
bazel build //orders:uber

# Menu module (Go/Gin)
bazel build //menu:artifact
bazel test //menu:unit
bazel test //menu:integration
bazel build //menu:uber
bazel build //menu:layer
bazel build //menu:image

# Customers module
bazel build //customers:artifact
bazel build //customers:uber

# Payments module
bazel build //payments:artifact
bazel build //payments:uber

# Notifications module
bazel build //notifications:artifact
bazel test //notifications:unit
bazel build //notifications:uber

# Supplies module (TypeScript/NestJS)
bazel build //supplies:artifact
bazel build //supplies:uber
# Note: Image targets are manual (not built automatically)
bazel build //supplies:image --build_tag_filters=manual

# Deliveries module (Rust/Axum)
bazel build //deliveries:artifact
bazel build //deliveries:uber
bazel build //deliveries:image

# Build all artifacts
bazel build //...

# Build all uber targets (final executables)
bazel build //...:uber
```