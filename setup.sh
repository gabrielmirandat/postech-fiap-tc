#! /bin/sh

docker exec -it postech_kafka kafka-topics --create --topic menub --bootstrap-server localhost:9092

#docker exec -it postech_kafka kafka-topics --describe --topic menu --bootstrap-server localhost:9092

echo '{"id":"menuProductAdded","event":{"productID":"123","name":"Pizza","value":"10.99"}}' | docker exec \
-i postech_kafka kafka-console-producer --bootstrap-server localhost:9092 --topic menu
