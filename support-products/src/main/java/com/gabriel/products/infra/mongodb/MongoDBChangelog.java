package com.gabriel.products.infra.mongodb;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.runner.core.event.MigrationFailureEvent;
import io.mongock.runner.core.event.MigrationStartedEvent;
import io.mongock.runner.core.event.MigrationSuccessEvent;
import org.bson.Document;

@ChangeUnit(id = "createInitialCollections", order = "001", author = "mongock")
public class MongoDBChangelog {

    public static void onStart(MigrationStartedEvent event) {
        System.out.println("[EVENT LISTENER] - Mongock STARTED successfully");
    }

    public static void onSuccess(MigrationSuccessEvent event) {
        System.out.println("[EVENT LISTENER] - Mongock finished successfully");
    }

    public static void onFail(MigrationFailureEvent event) {
        System.out.println("[EVENT LISTENER] - Mongock finished with failures: "
            + event.getMigrationResult().getException().getMessage());
    }

    @Execution
    public void createCollections(MongoDatabase db) {
        createIngredientCollection(db);
        createProductCollection(db);
    }

    protected void createIngredientCollection(MongoDatabase db) {
        // Create collection if it doesn't exist
        db.createCollection("ingredients");

        // Create index
        db.getCollection("ingredients")
            .createIndex(new Document("name", 1), new IndexOptions().unique(true));
    }

    protected void createProductCollection(MongoDatabase db) {
        // Create collection if it doesn't exist
        db.createCollection("products");

        // Create index
        db.getCollection("products")
            .createIndex(new Document("name", 1), new IndexOptions().unique(true));
    }
}
