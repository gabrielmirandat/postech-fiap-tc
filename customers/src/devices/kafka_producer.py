import uuid
from datetime import datetime
from aiokafka import AIOKafkaProducer
import json

class KafkaProducerDevice:
    def __init__(self, bootstrap_servers: str):
        self.bootstrap_servers = bootstrap_servers
        self.producer = None

    async def start(self):
        if self.producer is None:
            self.producer = AIOKafkaProducer(
                bootstrap_servers=self.bootstrap_servers,
                value_serializer=lambda v: json.dumps(v).encode('utf-8')
            )
            await self.producer.start()

    async def stop(self):
        if self.producer:
            await self.producer.stop()
            self.producer = None

    async def send_cloudevent(self, topic: str, data: dict, source: str, event_type: str):
        if self.producer is None:
            await self.start()
            
        cloud_event = {
            "specversion": "1.0",
            "type": event_type,
            "source": source,
            "id": str(uuid.uuid4()),
            "time": datetime.utcnow().isoformat() + "Z",
            "datacontenttype": "application/json",
            "data": data,
        }
        await self.producer.send_and_wait(topic, value=cloud_event)