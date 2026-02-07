require 'kafka'

seed_brokers = ENV.fetch("KAFKA_SERVER_URL", "localhost:9092").split(',')

KAFKA_CLIENT = Kafka.new(
  seed_brokers: seed_brokers,
  client_id: "notifications-service"
)
