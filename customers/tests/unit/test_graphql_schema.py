"""Unit tests for GraphQL schema using Strawberry"""
import unittest
from unittest.mock import AsyncMock, MagicMock

import strawberry
from strawberry.schema import Schema

from src.entities.customer import Customer
from src.graphql.schema import (
    CustomerInput,
    CustomerType,
    Mutation,
    Query,
    schema,
)


class TestCustomerType(unittest.TestCase):
    """Tests for CustomerType Strawberry type"""

    def test_from_entity(self):
        entity = Customer(gov_id="123.456.789-00", name="John", email="john@test.com")
        result = CustomerType.from_entity(entity)

        self.assertEqual(result.gov_id, "123.456.789-00")
        self.assertEqual(result.name, "John")
        self.assertEqual(result.email, "john@test.com")

    def test_from_entity_preserves_all_fields(self):
        entity = Customer(gov_id="", name="", email="")
        result = CustomerType.from_entity(entity)

        self.assertEqual(result.gov_id, "")
        self.assertEqual(result.name, "")
        self.assertEqual(result.email, "")


class TestSchema(unittest.TestCase):
    """Tests for the Strawberry schema definition"""

    def test_schema_is_valid(self):
        self.assertIsInstance(schema, Schema)

    def test_schema_has_query(self):
        result = schema.execute_sync(
            "query { __schema { queryType { name } } }"
        )
        self.assertIsNone(result.errors)
        self.assertEqual(result.data["__schema"]["queryType"]["name"], "Query")

    def test_schema_has_mutation(self):
        result = schema.execute_sync(
            "query { __schema { mutationType { name } } }"
        )
        self.assertIsNone(result.errors)
        self.assertEqual(result.data["__schema"]["mutationType"]["name"], "Mutation")


class TestGraphQLQuery(unittest.IsolatedAsyncioTestCase):
    """Tests for GraphQL query execution"""

    async def test_query_customer_success(self):
        mock_use_case = AsyncMock()
        mock_use_case.get_customer_by_id.return_value = Customer(
            gov_id="123.456.789-00", name="John Doe", email="john@example.com"
        )

        result = await schema.execute(
            """
            query {
                customer(govId: "123.456.789-00") {
                    govId
                    name
                    email
                }
            }
            """,
            context_value={"use_case": mock_use_case},
        )

        self.assertIsNone(result.errors)
        self.assertEqual(result.data["customer"]["govId"], "123.456.789-00")
        self.assertEqual(result.data["customer"]["name"], "John Doe")
        self.assertEqual(result.data["customer"]["email"], "john@example.com")
        mock_use_case.get_customer_by_id.assert_called_once_with("123.456.789-00")

    async def test_query_customer_not_found(self):
        mock_use_case = AsyncMock()
        mock_use_case.get_customer_by_id.side_effect = ValueError(
            "Customer with govId 999 not found."
        )

        result = await schema.execute(
            """
            query {
                customer(govId: "999") {
                    govId
                    name
                    email
                }
            }
            """,
            context_value={"use_case": mock_use_case},
        )

        self.assertIsNotNone(result.errors)
        self.assertIn("not found", result.errors[0].message)


class TestGraphQLMutation(unittest.IsolatedAsyncioTestCase):
    """Tests for GraphQL mutation execution"""

    async def test_create_customer_success(self):
        mock_use_case = AsyncMock()
        mock_use_case.create_customer.return_value = Customer(
            gov_id="123.456.789-00", name="Jane Doe", email="jane@example.com"
        )

        result = await schema.execute(
            """
            mutation {
                createCustomer(input: {
                    govId: "123.456.789-00",
                    name: "Jane Doe",
                    email: "jane@example.com"
                }) {
                    govId
                    name
                    email
                }
            }
            """,
            context_value={"use_case": mock_use_case},
        )

        self.assertIsNone(result.errors)
        self.assertEqual(result.data["createCustomer"]["govId"], "123.456.789-00")
        self.assertEqual(result.data["createCustomer"]["name"], "Jane Doe")
        self.assertEqual(result.data["createCustomer"]["email"], "jane@example.com")
        mock_use_case.create_customer.assert_called_once_with(
            "123.456.789-00", "Jane Doe", "jane@example.com"
        )

    async def test_create_customer_validation_error(self):
        mock_use_case = AsyncMock()
        mock_use_case.create_customer.side_effect = ValueError(
            "All fields are required."
        )

        result = await schema.execute(
            """
            mutation {
                createCustomer(input: {
                    govId: "",
                    name: "",
                    email: ""
                }) {
                    govId
                    name
                    email
                }
            }
            """,
            context_value={"use_case": mock_use_case},
        )

        self.assertIsNotNone(result.errors)
        self.assertIn("required", result.errors[0].message)


if __name__ == "__main__":
    unittest.main()
