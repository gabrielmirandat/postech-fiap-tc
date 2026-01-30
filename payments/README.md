# Payments Module

Payment processing microservice built with .NET 8.0, following Clean Architecture and Domain-Driven Design principles.

## Architecture

- **Domain Layer**: Core business entities, value objects, and domain events
- **Application Layer**: Use cases, commands, and DTOs using MediatR
- **Infrastructure Layer**: Cassandra persistence, Stripe integration, Kafka event publishing
- **API Layer**: REST and gRPC endpoints

## Technologies

- **.NET 8.0**: Framework
- **Cassandra**: Distributed database for payment storage
- **Stripe**: Payment gateway integration
- **Kafka**: Event-driven messaging
- **MediatR**: CQRS pattern implementation
- **gRPC**: Inter-service communication

## Project Structure

```
Payments.sln
├── Payments.Domain
│   ├── Entities/
│   │   └── Payment.cs
│   ├── ValueObjects/
│   │   ├── Money.cs
│   │   └── PaymentId.cs
│   ├── Enums/
│   │   └── PaymentStatus.cs
│   ├── Repositories/
│   │   └── IPaymentRepository.cs
│   ├── Services/
│   │   └── IPaymentAuthorizationService.cs
│   └── Events/
│       ├── IDomainEvent.cs
│       ├── IEventPublisher.cs
│       ├── PaymentCreatedEvent.cs
│       ├── PaymentCompletedEvent.cs
│       └── PaymentFailedEvent.cs
│
├── Payments.Application
│   ├── UseCases/
│   │   ├── CreatePayment/
│   │   │   ├── CreatePaymentCommand.cs
│   │   │   └── CreatePaymentHandler.cs
│   │   └── AuthorizePayment/
│   │       ├── AuthorizePaymentCommand.cs
│   │       └── AuthorizePaymentHandler.cs
│   └── DTOs/
│       └── PaymentDto.cs
│
├── Payments.Infrastructure
│   ├── Persistence/
│   │   ├── PaymentDbContext.cs
│   │   ├── PaymentEntity.cs
│   │   └── PaymentRepository.cs
│   ├── Gateways/
│   │   └── StripePaymentAuthorizationService.cs
│   ├── Messaging/
│   │   ├── KafkaEventPublisher.cs
│   │   └── Mappers/
│   │       └── CloudEventMapper.cs
│   └── Configuration/
│       ├── CassandraConfiguration.cs
│       └── DependencyInjection.cs
│
└── Payments.Api
    ├── Controllers/
    │   └── PaymentsController.cs
    ├── Protos/
    │   └── payments.proto
    └── Program.cs
```

## Configuration

Set the following environment variables or update `appsettings.json`:

- `Cassandra:ContactPoints`: Cassandra cluster contact points
- `Cassandra:Keyspace`: Keyspace name (default: "payments")
- `Stripe:ApiKey`: Stripe API key
- `Kafka:BootstrapServers`: Kafka broker addresses

## Building

### Using .NET CLI

```bash
dotnet build
dotnet run --project Payments.API
```

### Using Bazel

```bash
bazel build //payments:payments_api
```

## Docker

```bash
docker build -t payments-service .
docker run -p 8080:80 payments-service
```

## TODO

- [ ] Implement SAGA pattern for distributed transaction orchestration
- [ ] Add comprehensive unit and integration tests
- [ ] Implement GetPayment query handler
- [ ] Add gRPC service implementation
- [ ] Add OpenTelemetry instrumentation
- [ ] Implement payment retry logic
- [ ] Add payment webhook handling from Stripe
