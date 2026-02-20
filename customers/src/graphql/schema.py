import strawberry
from strawberry.types import Info
from src.entities.customer import Customer
from src.use_cases.customer_use_case import CustomerUseCase


@strawberry.type
class CustomerType:
    gov_id: str
    name: str
    email: str

    @classmethod
    def from_entity(cls, customer: Customer) -> "CustomerType":
        return cls(
            gov_id=customer.gov_id,
            name=customer.name,
            email=customer.email,
        )


@strawberry.input
class CustomerInput:
    gov_id: str
    name: str
    email: str


def get_use_case(info: Info) -> CustomerUseCase:
    return info.context["use_case"]


@strawberry.type
class Query:
    @strawberry.field
    async def customer(self, info: Info, gov_id: str) -> CustomerType:
        use_case = get_use_case(info)
        customer = await use_case.get_customer_by_id(gov_id)
        return CustomerType.from_entity(customer)


@strawberry.type
class Mutation:
    @strawberry.mutation
    async def create_customer(self, info: Info, input: CustomerInput) -> CustomerType:
        use_case = get_use_case(info)
        customer = await use_case.create_customer(
            input.gov_id, input.name, input.email
        )
        return CustomerType.from_entity(customer)


schema = strawberry.Schema(query=Query, mutation=Mutation)
