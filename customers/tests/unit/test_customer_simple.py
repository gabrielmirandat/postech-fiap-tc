"""Simple unit tests for Customer entity using unittest (no external dependencies)"""
import unittest
import sys
import os

# Add src to path
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '../..'))

from src.entities.customer import Customer


class TestCustomerEntity(unittest.TestCase):
    """Unit tests for Customer entity"""

    def test_create_customer_with_valid_data(self):
        """Should create customer with valid data"""
        customer = Customer(
            gov_id="123.456.789-00",
            name="John Doe",
            email="john@example.com"
        )

        self.assertEqual(customer.gov_id, "123.456.789-00")
        self.assertEqual(customer.name, "John Doe")
        self.assertEqual(customer.email, "john@example.com")

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

        self.assertNotEqual(customer1.gov_id, customer2.gov_id)
        self.assertNotEqual(customer1.name, customer2.name)
        self.assertNotEqual(customer1.email, customer2.email)

    def test_customer_attributes_are_accessible(self):
        """Should access all customer attributes"""
        customer = Customer(
            gov_id="333.333.333-33",
            name="Charlie",
            email="charlie@example.com"
        )

        self.assertTrue(hasattr(customer, 'gov_id'))
        self.assertTrue(hasattr(customer, 'name'))
        self.assertTrue(hasattr(customer, 'email'))

    def test_customer_with_empty_values(self):
        """Should create customer even with empty values"""
        customer = Customer(
            gov_id="",
            name="",
            email=""
        )

        self.assertEqual(customer.gov_id, "")
        self.assertEqual(customer.name, "")
        self.assertEqual(customer.email, "")


if __name__ == '__main__':
    unittest.main()
