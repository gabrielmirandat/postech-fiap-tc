from fastapi import HTTPException
from src.use_cases.customer_use_case import CustomerUseCase
from src.presenters.customer_presenter import CustomerPresenter
from dependency_injector.wiring import inject, Provide
from src.containers import AppContainer


class CustomerController:
    """Handles API requests and responses for customer-related operations."""

    @inject
    def __init__(self, customer_use_case: CustomerUseCase = Provide[AppContainer.customer_use_case]):
        self.customer_use_case = customer_use_case

    async def create_customer(self, gov_id: str, name: str, email: str):
        """Handles the creation of a new customer."""
        try:
            customer = await self.customer_use_case.create_customer(gov_id, name, email)
            return CustomerPresenter.to_dict(customer)
        except ValueError as e:
            raise HTTPException(status_code=400, detail=CustomerPresenter.to_error_response(400, "BadRequest", str(e)))

    async def get_customer_by_id(self, gov_id: str):
        """Handles fetching a customer by their govId."""
        customer = await self.customer_use_case.get_customer_by_id(gov_id)
        if not customer:
            raise HTTPException(
                status_code=404,
                detail=CustomerPresenter.to_error_response(404, "NotFound", f"Customer with govId {gov_id} not found."),
            )
        return CustomerPresenter.to_dict(customer)