# gabsrell restaurant

Based on the software architecture specialization tech challenge,
this project is a restaurant management system.

## Modules

- infra (compose, kubernetes, terraform, aws academy, dockerhub, gitHub actions, SAGA [**TODO**])
- orders (ddd, hexagonal, springboot, kafka, grpc, mongodb [**TODO: change to Event Sourcing with EventStoreDB + CQRS
  **])
- menu (ddd, hexagonal, quarkus, kafka, grpc, mongodb)
- permissions with auth0 (ddd, mvc, springboot, postgree + audit table [**TODO**], kafka [**TODO**])
- customers [**TODO**] (ddd, clean arq, python, edgeDB, kafka)
- payments with stripe [**TODO**] (ddd, clean arq, dotnet, cassandra, kafka)

## Docs

https://miro.com/app/board/uXjVNf1J6J8=/?share_link_id=738234968069

## Bazel

```
    bazel clean --expunge
    
    bazel build //core:artifact
    bazel test //core:unit
    
    bazel build //permissions:artifact
    bazel build //permissions:uber_deploy.jar
    bazel build //permissions:image
    bazel run //permissions:push
    
    bazel build //orders:artifact
    bazel test //orders:unit
    bazel test //orders:integration
    bazel test //orders:contract
    bazel test //orders:behavior
    bazel build //orders:uber_deploy.jar
    bazel run //orders:uber
    bazel build //orders:image
    bazel run //orders:push
    
    bazel build //menu:artifact
    bazel test //menu:unit
    bazel build //menu:uber_deploy.jar
    bazel build //menu:image
    bazel run //menu:push
```

java -jar -Dspring.profiles.active=local bazel-bin/orders/uber_deploy.jar
java -jar bazel-bin/orders/uber_deploy.jar --spring.profiles.active=local

docker run --name permissions-container --network postech_network -p 8000:8000 -it gabrielmirandat/permissions:latest
docker run --name orders-container --network postech_network -p 8001:8001 -it gabrielmirandat/orders:latest
docker run --name menu-container --network postech_network -p 8002:8002 -it gabrielmirandat/menu:latest

https://www.reddit.com/r/bazel/comments/jb2fow/options_for_cicd_integration_with_bazel/