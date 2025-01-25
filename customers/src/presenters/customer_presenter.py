from typing import Dict
from src.entities.customer import Customer


class CustomerPresenter:
    """Responsible for formatting Customer entities into output representations."""

    @staticmethod
    def to_dict(customer: Customer) -> Dict[str, str]:
        """Converts a Customer entity into a dictionary for API responses."""
        return {
            "govId": customer.gov_id,
            "name": customer.name,
            "email": customer.email,
        }

    @staticmethod
    def to_error_response(code: int, error_type: str, message: str) -> Dict[str, str]:
        """Formats an error response."""
        return {
            "code": code,
            "type": error_type,
            "message": message,
        }