from fastapi import FastAPI
import uvicorn
from strawberry.fastapi import GraphQLRouter
from src.web.customer_web import router
from src.graphql.schema import schema
from src.containers import AppContainer
from src.db.init_schema import init_schema
import asyncio

container = AppContainer()

app = FastAPI()
app.container = container

app.include_router(router, tags=["customers"])


async def get_graphql_context():
    return {"use_case": container.customer_use_case()}


graphql_app = GraphQLRouter(schema, context_getter=get_graphql_context)
app.include_router(graphql_app, prefix="/graphql", tags=["graphql"])

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