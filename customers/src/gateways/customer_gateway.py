from typing import Optional
from src.entities.customer import Customer
from src.db.edgedb_client import EdgeDBClient

class CustomerGateway:
    def __init__(self, edgedb_client: EdgeDBClient):
        self.edgedb_client = edgedb_client

    async def create_customer(self, gov_id: str, name: str, email: str) -> Customer:
        customer = Customer(gov_id=gov_id, name=name, email=email)
        await self.edgedb_client.save_customer(customer)
        return customer

    async def get_customer_by_id(self, gov_id: str) -> Optional[Customer]:
        return await self.edgedb_client.find_customer_by_id(gov_id)