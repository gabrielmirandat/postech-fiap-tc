require 'kafka'

KAFKA_CLIENT = Kafka.new(
  seed_brokers: ENV.fetch("KAFKA_SERVER_URL", "localhost:9092").split(','),
  client_id: "notifications-service"
)

KAFKA_CONSUMER = KAFKA_CLIENT.consumer(
  group_id: ENV.fetch("KAFKA_GROUP_ID", "notifications-group-id")
)
