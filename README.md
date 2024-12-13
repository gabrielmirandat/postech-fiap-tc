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
    
    bazel build //orders:artifact
    bazel test //orders:unit
    bazel test //orders:integration
    bazel test //orders:contract
    bazel test //orders:behavior
    bazel build //orders:uber_deploy.jar
    bazel build //orders:image
    bazel run //orders:push
    
    bazel build //menu:artifact
    bazel test //menu:unit
    bazel build //menu:image
    bazel run //menu:push
    
    bazel build //permissions:artifact
    bazel build //permissions:image
    bazel run //permissions:push
```

docker run --name orders-container -p 8001:8001 --entrypoint "java -jar -Dspring.profiles.active=local \
orders/uber_deploy.jar > /dev/stdout 2> \
&1" -it gabrielmirandat/orders:latest

https://www.reddit.com/r/bazel/comments/jb2fow/options_for_cicd_integration_with_bazel/