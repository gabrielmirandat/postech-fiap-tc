import os

class Config:
    ENV = os.getenv("ENV", "local")
    
    KAFKA_BOOTSTRAP_SERVERS = "localhost:9092"
    EDGEDB_DSN = "edgedb://edgedb:dummy@localhost:5656/edgedb?tls_security=insecure"
    
    def __init__(self):
        if self.ENV == "docker":
            self.KAFKA_BOOTSTRAP_SERVERS = "kafka:29092"
            self.EDGEDB_DSN = "edgedb://edgedb:dummy@edgedb:5656/edgedb?tls_security=insecure"
        elif self.ENV == "k8s":
            self.KAFKA_BOOTSTRAP_SERVERS = "kafka-service:9092"
            self.EDGEDB_DSN = "edgedb://edgedb:dummy@edgedb-service:5656/edgedb?tls_security=insecure"
        
        if os.getenv("KAFKA_BOOTSTRAP_SERVERS"):
            self.KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS")
        if os.getenv("EDGEDB_DSN"):
            self.EDGEDB_DSN = os.getenv("EDGEDB_DSN")

config = Config() 