from fastapi import FastAPI
import uvicorn
from src.web.customer_web import router
from src.containers import AppContainer

container = AppContainer()

app = FastAPI()
app.container = container

app.include_router(router, prefix="/api/v1", tags=["customers"])

@app.on_event("startup")
async def startup():
    kafka_producer = container.kafka_producer()
    await kafka_producer.start()

@app.on_event("shutdown")
async def shutdown():
    kafka_producer = container.kafka_producer()
    await kafka_producer.stop()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)