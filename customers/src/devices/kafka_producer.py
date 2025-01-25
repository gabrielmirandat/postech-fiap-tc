import uuid
from datetime import datetime
from kafka import KafkaProducer
import json

class KafkaProducerDevice:
    """Kafka producer client for sending CloudEvents to Kafka topics."""

    def __init__(self, bootstrap_servers: str):
        """Initializes the Kafka producer."""
        self.producer = KafkaProducer(
            bootstrap_servers=bootstrap_servers,
            value_serializer=lambda v: json.dumps(v).encode('utf-8')
        )

    def send_cloudevent(self, topic: str, data: dict, source: str, event_type: str):
        """Sends a CloudEvent to the specified Kafka topic."""
        cloud_event = {
            "specversion": "1.0",
            "type": event_type,
            "source": source,
            "id": str(uuid.uuid4()),
            "time": datetime.utcnow().isoformat() + "Z",
            "datacontenttype": "application/json",
            "data": data,
        }
        # Send the event to Kafka
        self.producer.send(topic, value=cloud_event)
        self.producer.flush()