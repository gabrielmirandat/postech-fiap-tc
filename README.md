# postech-fiap-tc
PosTech Software Architecture Tech Challenge

DDD, Ports & Adapters (Hexagonal) architecture, Clean Architecture

- Add CQRS, two databases for each aggregate, one command (add, update, delete) and one query (searchs)
  - Query database must be updated using event sourcing from command operations
  - Events must also be persisted, so it is possible to re-generate each database 
