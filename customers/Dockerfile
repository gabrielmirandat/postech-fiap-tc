FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

# Set environment to docker when running in containers
ENV ENV=docker

EXPOSE 8003

CMD ["python", "-m", "src.main"] 