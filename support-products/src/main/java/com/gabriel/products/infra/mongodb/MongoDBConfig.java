package com.gabriel.products.infra.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.bson.Document;

@ApplicationScoped
public class MongoDBConfig {

    @Inject
    MongoClient mongoClient;

    @Produces
    @Named("productCollection")
    public MongoCollection<Document> getProductCollection() {
        MongoDatabase database = mongoClient.getDatabase("postech_db");
        return database.getCollection("products");
    }

    @Produces
    @Named("ingredientCollection")
    public MongoCollection<Document> getIngredientCollection() {
        MongoDatabase database = mongoClient.getDatabase("postech_db");
        return database.getCollection("ingredients");
    }
}
