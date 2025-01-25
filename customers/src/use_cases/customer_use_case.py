from typing import Optional
from src.entities.customer import Customer
from src.gateways.customer_gateway import CustomerGateway


class CustomerUseCase:
    """Business logic for managing customers."""

    def __init__(self, customer_gateway: CustomerGateway):
        self.customer_gateway = customer_gateway

    async def create_customer(self, gov_id: str, name: str, email: str) -> Customer:
        """Handles the creation of a new customer."""
        # Validate customer data or apply any business rules
        if not gov_id or not name or not email:
            raise ValueError("All fields are required.")

        # Create the customer by calling the gateway method
        customer = await self.customer_gateway.create_customer(gov_id, name, email)

        # You could add additional logic such as sending an event, logging, etc.
        return customer

    async def get_customer_by_id(self, gov_id: str) -> Optional[Customer]:
        """Handles fetching a customer by their govId."""
        # Fetch the customer by govId from the gateway
        customer = await self.customer_gateway.get_customer_by_id(gov_id)

        # Additional business logic could be added here, like logging or validations
        if not customer:
            raise ValueError(f"Customer with govId {gov_id} not found.")
        
        return customer