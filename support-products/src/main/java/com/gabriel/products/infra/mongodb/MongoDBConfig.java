package com.gabriel.products.infra.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.runner.standalone.MongockStandalone;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MongoDBConfig {

    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String mongodbDatabase;


    @Produces
    @Named("productCollection")
    public MongoCollection<Document> getProductCollection() {
        MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
        return database.getCollection("products");
    }

    @Produces
    @Named("ingredientCollection")
    public MongoCollection<Document> getIngredientCollection() {
        MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
        return database.getCollection("ingredients");
    }

    @PostConstruct
    public void onStartup() {
        MongoSync4Driver driver = MongoSync4Driver.withDefaultLock(mongoClient, mongodbDatabase);

        MongockStandalone.builder()
            .setDriver(driver)
            .addMigrationScanPackage("com.gabriel.products.infra.mongodb")
            .buildRunner().execute();
    }
}
