from fastapi import APIRouter, HTTPException, status
from src.use_cases.customer_use_case import CustomerUseCase
from src.entities.customer import Customer
from pydantic import BaseModel

class CustomerRequest(BaseModel):
    govId: str
    name: str
    email: str

class CustomerResponse(BaseModel):
    govId: str
    name: str
    email: str

router = APIRouter()

@router.post("/customers", status_code=status.HTTP_201_CREATED, response_model=CustomerResponse)
async def create_customer(customer_data: CustomerRequest, use_case: CustomerUseCase):
    try:
        customer = await use_case.create_customer(
            customer_data.govId, 
            customer_data.name, 
            customer_data.email
        )
        return CustomerResponse(
            govId=customer.gov_id,
            name=customer.name,
            email=customer.email
        )
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail=str(e))

@router.head("/customers/{gov_id}", status_code=status.HTTP_200_OK)
async def get_customer_by_id(gov_id: str, use_case: CustomerUseCase):
    customer = await use_case.get_customer_by_id(gov_id)
    if not customer:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Customer not found")
    return {"govId": customer.gov_id, "name": customer.name, "email": customer.email}