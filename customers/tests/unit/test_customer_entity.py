import pytest
from src.entities.customer import Customer


class TestCustomerEntity:
    """Unit tests for Customer entity"""

    def test_create_customer_with_valid_data(self):
        """Should create customer with valid data"""
        customer = Customer(
            gov_id="123.456.789-00",
            name="John Doe",
            email="john@example.com"
        )

        assert customer.gov_id == "123.456.789-00"
        assert customer.name == "John Doe"
        assert customer.email == "john@example.com"

    def test_create_customer_with_different_data(self):
        """Should create multiple customers with different data"""
        customer1 = Customer(
            gov_id="111.111.111-11",
            name="Alice",
            email="alice@test.com"
        )

        customer2 = Customer(
            gov_id="222.222.222-22",
            name="Bob",
            email="bob@test.com"
        )

        assert customer1.gov_id != customer2.gov_id
        assert customer1.name != customer2.name
        assert customer1.email != customer2.email

    def test_customer_attributes_are_accessible(self):
        """Should access all customer attributes"""
        customer = Customer(
            gov_id="333.333.333-33",
            name="Charlie",
            email="charlie@example.com"
        )

        assert hasattr(customer, 'gov_id')
        assert hasattr(customer, 'name')
        assert hasattr(customer, 'email')

    def test_customer_with_empty_values(self):
        """Should create customer even with empty values (validation is in use case)"""
        customer = Customer(
            gov_id="",
            name="",
            email=""
        )

        assert customer.gov_id == ""
        assert customer.name == ""
        assert customer.email == ""
