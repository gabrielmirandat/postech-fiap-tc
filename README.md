# postech-fiap-tc
PosTech Software Architecture Tech Challenge

## Technologies

DDD, Ports & Adapters (Hexagonal) architecture, Clean Architecture

### Databases
- Add CQRS, two databases for each aggregate, one command (add, update, delete) and one query (searchs)
  - Query database must be updated using event sourcing from command operations
  - Query database must also have last updated field to clients
  - Events must also be persisted, so it is possible to re-generate each database

### APIs
- API Versioning, with content negotiation
- Accept: application/vbd.example.api+json;version=2
x- API generated with Swagger [https://www.youtube.com/watch?v=YmQyzNF5iKg, https://github.com/swagger-api/swagger-codegen],
- DDD automatic doc generation, Redoc
- OpenTelemetry (logs, metrics & traces), Grafana

### Queues
- Kafka, or SQS with SNS

### Tests
- Integration (serialize, deserialize data), contract (between apis), acceptance (user)

### Security
- Add Spring automatic security

## TECH CHALLENGE

https://miro.com/app/board/uXjVNf1J6J8=/?share_link_id=738234968069

