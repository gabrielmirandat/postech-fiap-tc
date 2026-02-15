package config

import (
	"os"
	"strconv"
)

type Config struct {
	HTTPPort string
	MongoURI string
	MongoDB  string
	KafkaBrokers string
}

func Load() Config {
	port := os.Getenv("HTTP_PORT")
	if port == "" {
		port = "8002"
	}
	mongoURI := os.Getenv("MONGODB_CONN_STRING")
	if mongoURI == "" {
		mongoURI = "mongodb://localhost:27017"
	}
	mongoDB := os.Getenv("MONGODB_CONN_DB")
	if mongoDB == "" {
		mongoDB = "postech_db"
	}
	kafkaBrokers := os.Getenv("KAFKA_SERVER_URL")
	if kafkaBrokers == "" {
		kafkaBrokers = "localhost:9092"
	}
	return Config{
		HTTPPort:     port,
		MongoURI:     mongoURI,
		MongoDB:      mongoDB,
		KafkaBrokers: kafkaBrokers,
	}
}

func (c Config) PortInt() int {
	p, _ := strconv.Atoi(c.HTTPPort)
	if p == 0 {
		p = 8002
	}
	return p
}
