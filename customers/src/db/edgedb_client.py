import edgedb
from src.entities.customer import Customer

class EdgeDBClient:
    """Client for interacting with the EdgeDB database."""

    def __init__(self):
        """Initializes the EdgeDB client."""
        self.client = edgedb.create_client()

    async def save_customer(self, customer: Customer):
        """Saves a new customer into the database."""
        query = """
            INSERT Customer {
                govId := <str>$gov_id,
                name := <str>$name,
                email := <str>$email
            }
        """
        # Execute the query to save the customer into EdgeDB
        await self.client.query(query, gov_id=customer.gov_id, name=customer.name, email=customer.email)

    async def find_customer_by_id(self, gov_id: str) -> Customer:
        """Finds a customer by their govId."""
        query = """
            SELECT Customer {
                govId,
                name,
                email
            } FILTER .govId = <str>$gov_id
        """
        result = await self.client.query_single(query, gov_id=gov_id)
        
        # If the result is not found, return None
        if not result:
            return None
        
        # Return a Customer object with the found result
        return Customer(result.govId, result.name, result.email)