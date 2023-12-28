package com.gabriel.products.infra.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class MongoDBConfig {

    @Inject
    MongoClient mongoClient;

    @Produces
    public MongoCollection getProductCollection() {
        MongoDatabase database = mongoClient.getDatabase("postech_db");
        return database.getCollection("products");
    }

    @Produces
    public MongoCollection getIngredientCollection() {
        MongoDatabase database = mongoClient.getDatabase("postech_db");
        return database.getCollection("ingredients");
    }
}
