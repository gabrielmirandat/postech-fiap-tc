from typing import Optional
from src.entities.customer import Customer
from src.gateways.customer_gateway import CustomerGateway
from src.devices.kafka_producer import KafkaProducerDevice


class CustomerUseCase:
    def __init__(self, gateway: CustomerGateway, kafka_producer: KafkaProducerDevice):
        self.customer_gateway = gateway
        self.kafka_producer = kafka_producer

    async def create_customer(self, gov_id: str, name: str, email: str) -> Customer:
        if not gov_id or not name or not email:
            raise ValueError("All fields are required.")
        customer = await self.customer_gateway.create_customer(gov_id, name, email)
        # Publish customer created event
        await self.kafka_producer.send_message(
            topic="customer-events",
            key=gov_id,
            value={"event": "customer_created", "data": {"gov_id": gov_id, "name": name, "email": email}}
        )
        return customer

    async def get_customer_by_id(self, gov_id: str) -> Optional[Customer]:
        customer = await self.customer_gateway.get_customer_by_id(gov_id)
        if not customer:
            raise ValueError(f"Customer with govId {gov_id} not found.")
        return customer