import edgedb
from src.entities.customer import Customer

class EdgeDBClient:
    def __init__(self, dsn: str):
        # Use async client for async operations
        self.client = edgedb.create_async_client(dsn=dsn)

    async def save_customer(self, customer: Customer):
        query = """
            INSERT default::Customer {
                govId := <str>$gov_id,
                name := <str>$name,
                email := <str>$email
            }
        """
        await self.client.query(query, gov_id=customer.gov_id, name=customer.name, email=customer.email)

    async def find_customer_by_id(self, gov_id: str) -> Customer:
        query = """
            SELECT default::Customer {
                govId,
                name,
                email
            } FILTER .govId = <str>$gov_id
        """
        result = await self.client.query_single(query, gov_id=gov_id)
        if not result:
            return None
        return Customer(gov_id=result.govId, name=result.name, email=result.email)