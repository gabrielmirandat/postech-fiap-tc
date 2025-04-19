from typing import Dict
from src.entities.customer import Customer


class CustomerPresenter:

    @staticmethod
    def to_dict(customer: Customer) -> Dict[str, str]:
        return {
            "govId": customer.gov_id,
            "name": customer.name,
            "email": customer.email,
        }

    @staticmethod
    def to_error_response(code: int, error_type: str, message: str) -> Dict[str, str]:
        return {
            "code": code,
            "type": error_type,
            "message": message,
        }