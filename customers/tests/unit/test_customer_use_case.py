import pytest
from unittest.mock import AsyncMock, MagicMock
from src.use_cases.customer_use_case import CustomerUseCase
from src.entities.customer import Customer


class TestCustomerUseCase:
    """Unit tests for CustomerUseCase"""

    @pytest.fixture
    def mock_gateway(self):
        """Create a mock CustomerGateway"""
        gateway = MagicMock()
        gateway.create_customer = AsyncMock()
        gateway.get_customer_by_id = AsyncMock()
        return gateway

    @pytest.fixture
    def mock_kafka_producer(self):
        """Create a mock KafkaProducerDevice"""
        producer = MagicMock()
        producer.send_cloudevent = AsyncMock()
        return producer

    @pytest.fixture
    def use_case(self, mock_gateway, mock_kafka_producer):
        """Create CustomerUseCase with mocked dependencies"""
        return CustomerUseCase(mock_gateway, mock_kafka_producer)

    async def test_create_customer_with_valid_data(self, use_case, mock_gateway, mock_kafka_producer):
        """Should create customer with valid data and publish event"""
        # Arrange
        expected_customer = Customer(
            gov_id="123.456.789-00",
            name="John Doe",
            email="john@example.com"
        )
        mock_gateway.create_customer.return_value = expected_customer

        # Act
        result = await use_case.create_customer(
            gov_id="123.456.789-00",
            name="John Doe",
            email="john@example.com"
        )

        # Assert
        assert result.gov_id == "123.456.789-00"
        assert result.name == "John Doe"
        assert result.email == "john@example.com"

        mock_gateway.create_customer.assert_called_once_with(
            "123.456.789-00",
            "John Doe",
            "john@example.com"
        )

        mock_kafka_producer.send_cloudevent.assert_called_once()

    async def test_create_customer_validates_required_fields(self, use_case):
        """Should raise ValueError when required fields are missing"""
        with pytest.raises(ValueError, match="All fields are required"):
            await use_case.create_customer(gov_id="", name="John", email="john@test.com")

        with pytest.raises(ValueError, match="All fields are required"):
            await use_case.create_customer(gov_id="123", name="", email="john@test.com")

        with pytest.raises(ValueError, match="All fields are required"):
            await use_case.create_customer(gov_id="123", name="John", email="")

    async def test_create_customer_publishes_kafka_event(self, use_case, mock_gateway, mock_kafka_producer):
        """Should publish Kafka CloudEvent when customer is created"""
        # Arrange
        expected_customer = Customer(
            gov_id="999.999.999-99",
            name="Jane Doe",
            email="jane@example.com"
        )
        mock_gateway.create_customer.return_value = expected_customer

        # Act
        await use_case.create_customer(
            gov_id="999.999.999-99",
            name="Jane Doe",
            email="jane@example.com"
        )

        # Assert
        mock_kafka_producer.send_cloudevent.assert_called_once_with(
            topic="customer-events",
            data={
                "gov_id": "999.999.999-99",
                "name": "Jane Doe",
                "email": "jane@example.com"
            },
            source="customers-service",
            event_type="customer.created"
        )

    async def test_get_customer_by_id_returns_customer(self, use_case, mock_gateway):
        """Should return customer when found"""
        # Arrange
        expected_customer = Customer(
            gov_id="123.456.789-00",
            name="John Doe",
            email="john@example.com"
        )
        mock_gateway.get_customer_by_id.return_value = expected_customer

        # Act
        result = await use_case.get_customer_by_id("123.456.789-00")

        # Assert
        assert result.gov_id == "123.456.789-00"
        assert result.name == "John Doe"
        mock_gateway.get_customer_by_id.assert_called_once_with("123.456.789-00")

    async def test_get_customer_by_id_raises_when_not_found(self, use_case, mock_gateway):
        """Should raise ValueError when customer is not found"""
        # Arrange
        mock_gateway.get_customer_by_id.return_value = None

        # Act & Assert
        with pytest.raises(ValueError, match="Customer with govId 999 not found"):
            await use_case.get_customer_by_id("999")

    async def test_use_case_uses_injected_dependencies(self, mock_gateway, mock_kafka_producer):
        """Should use injected gateway and kafka producer"""
        # Arrange
        use_case = CustomerUseCase(mock_gateway, mock_kafka_producer)

        # Assert
        assert use_case.customer_gateway == mock_gateway
        assert use_case.kafka_producer == mock_kafka_producer
