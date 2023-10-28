# postech-fiap-tc
PosTech Software Architecture Tech Challenge

## Technologies

DDD, Ports & Adapters (Hexagonal) architecture, Clean Architecture

### Databases
- Add CQRS, two databases for each aggregate, one command (add, update, delete) and one query (searchs)
  - Query database must be updated using event sourcing from command operations
  - Query database must also have last updated field to clients
  - Events must also be persisted, so it is possible to re-generate each database
- BANCO NAO RELACIONAL - SALVA PEDIDOS AS IS COM TUDO QUE PRECISA SEM PREOCUPAR COM 
NORMALIZACAO, SEM PRECISAR DE JOIN
- BANCO RELACIONAL - SALVA ESTADO DE CLIENTE NORMALIZADO COM DADO ESTRUTURADO
- EVENT SOURCING - CADA MUDANCA NUM AGREGADO GERA UM DOMAIN EVENT QUE EH PERSISTIDO
NO DOMAIN STORE. AO BUSCAR PELO AGREGADO, TODOS OS EVENTOS SAO CARREGADOS PARA 
CONSTITUIR O AGREGADO. PARA EVITAR LATENCIA, SALVAR TBM O SNAPSHOT DO AGREGATE A 
CADA X EVENTOS, A DEPENDER DA OBSERVABILIDADE.
- EVENT STORE N USA ORM E SALVA OS DADOS COMO BINARIO, N HA QUERY. APENAS O REPOSITORY
PRECISA DE UMA SIMPLES GET FIND OPERATION USANDO ID DO AGREGADO
- JUNTO COM O MODELO CQRS, A CADA NOVO EVENTO ATUALIZAR O QUERY MODEL JUNTO COM
TIMESTAMP DE UPDATE.

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

