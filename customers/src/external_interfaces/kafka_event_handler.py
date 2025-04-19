import json
from kafka import KafkaConsumer
from src.use_cases.customer_use_case import CustomerUseCase
from dependency_injector.wiring import inject, Provide
from src.containers import AppContainer


class KafkaEventHandler:
    @inject
    def __init__(self, customer_use_case: CustomerUseCase = Provide[AppContainer.customer_use_case]):
        self.customer_use_case = customer_use_case

    def start_consumer(self, topic: str, bootstrap_servers: str, group_id: str):
        consumer = KafkaConsumer(
            topic,
            bootstrap_servers=bootstrap_servers,
            group_id=group_id,
            value_deserializer=lambda v: json.loads(v.decode('utf-8')),
        )
        print(f"Kafka consumer started for topic: {topic}")
        for message in consumer:
            self.handle_event(message.value)

    def handle_event(self, event: dict):
        try:
            print(f"Received event: {event}")
            if not all(key in event for key in ["specversion", "type", "source", "id", "data"]):
                print("Invalid CloudEvent format")
                return

            event_type = event["type"]
            event_data = event["data"]

            if event_type == "customer.created":
                self.process_customer_created(event_data)
            else:
                print(f"Unhandled event type: {event_type}")
        except Exception as e:
            print(f"Error handling event: {e}")

    def process_customer_created(self, data: dict):
        try:
            print(f"Processing customer.created event with data: {data}")
            self.customer_use_case.process_new_customer(data)
        except Exception as e:
            print(f"Error processing customer.created event: {e}")