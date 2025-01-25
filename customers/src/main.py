import uvicorn
from fastapi import FastAPI
from src.web.customer_web import router
from src.containers import AppContainer

container = AppContainer()

app = FastAPI()
app.container = container

app.include_router(router, prefix="/api/v1", tags=["customers"])

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)