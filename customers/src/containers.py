from dependency_injector import containers, providers
from src.controllers.customer_controller import CustomerController
from src.db.edgedb_client import EdgeDBClient
from src.devices.kafka_producer import KafkaProducerDevice
from src.external_interfaces.kafka_event_handler import KafkaEventHandler
from src.gateways.customer_gateway import CustomerGateway
from src.use_cases.customer_use_case import CustomerUseCase

class AppContainer(containers.DeclarativeContainer):
    db_client = providers.Singleton(EdgeDBClient)
    kafka_producer = providers.Singleton(KafkaProducerDevice, bootstrap_servers="localhost:9092")
    customer_gateway = providers.Factory(CustomerGateway, db_client=db_client)
    customer_use_case = providers.Factory(CustomerUseCase, gateway=customer_gateway, kafka_producer=kafka_producer)
    customer_controller = providers.Factory(CustomerController, use_case=customer_use_case)
    kafka_event_handler = providers.Factory(KafkaEventHandler, use_case=customer_use_case)