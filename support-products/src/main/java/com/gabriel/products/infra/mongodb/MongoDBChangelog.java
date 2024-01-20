package com.gabriel.products.infra.mongodb;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

@ChangeLog(order = "001")
public class MongoDBChangelog {

    @ChangeSet(order = "001", id = "createInitialCollections", author = "com.gabriel")
    public void createCollections(MongoDatabase db) {
        createIngredientCollection(db);
        createProductCollection(db);
    }

    private void createIngredientCollection(MongoDatabase db) {
        // Create collection if it doesn't exist
        db.createCollection("ingredients");

        // Create index
        db.getCollection("ingredients")
            .createIndex(new Document("name", 1), new IndexOptions().unique(true));
    }

    private void createProductCollection(MongoDatabase db) {
        // Create collection if it doesn't exist
        db.createCollection("products");

        // Create index
        db.getCollection("products")
            .createIndex(new Document("name", 1), new IndexOptions().unique(true));
    }
}
