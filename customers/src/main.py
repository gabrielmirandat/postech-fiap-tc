from fastapi import FastAPI
import uvicorn
from src.web.customer_web import router
from src.containers import AppContainer
from src.db.init_schema import init_schema
import asyncio

container = AppContainer()

app = FastAPI()
app.container = container

app.include_router(router, tags=["customers"])

@app.on_event("startup")
async def startup():
    # Initialize schema first to ensure it exists
    print("Initializing EdgeDB schema...")
    await init_schema()
    print("Schema initialization completed")
    
    # Then start Kafka
    print("Starting Kafka producer...")
    kafka_producer = container.kafka_producer()
    await kafka_producer.start()
    print("Kafka producer started")

@app.on_event("shutdown")
async def shutdown():
    kafka_producer = container.kafka_producer()
    await kafka_producer.stop()

if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=8003)