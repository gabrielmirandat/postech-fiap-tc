# docker exec -it postech_kafka kafka-topics --create --topic quickstart-topic --bootstrap-server localhost:9092

# docker exec -it postech_kafka kafka-topics --describe --topic quickstart-topic --bootstrap-server localhost:9092

echo '{"id":"menuProductAdded","event":{"productID":"123","name":"Pizza","value":"10.99"}}' | docker exec \
-i postech_kafka kafka-console-producer --broker-list postech_kafka:9092 --topic quickstart-topic



#docker exec postech_kafka kafka-console-producer --topic quickstart-topic --bootstrap-server localhost:9092 <<EOF
#{"id":"menuProductUpdated","event":{"productID":"123","name":"Updated Pizza"}}
#EOF
#
#docker exec postech_kafka kafka-console-producer --topic quickstart-topic --bootstrap-server localhost:9092 <<EOF
#{"id":"menuProductDeleted","event":{"productID":"123"}}
#EOF


