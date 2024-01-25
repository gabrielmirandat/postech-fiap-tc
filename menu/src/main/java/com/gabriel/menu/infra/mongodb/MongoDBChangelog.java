package com.gabriel.menu.infra.mongodb;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

@ChangeUnit(order = "001", id = "createInitialCollections", author = "com.gabriel")
public class MongoDBChangelog {

    @Execution
    public void createInitialCollections(MongoDatabase db) {
        db.createCollection("ingredients");
        db.getCollection("ingredients").createIndex(new Document("name", 1), new IndexOptions().unique(true));
        db.createCollection("products");
        db.getCollection("products").createIndex(new Document("name", 1), new IndexOptions().unique(true));
    }

    @RollbackExecution
    public void rollback(MongoDatabase db) {
        db.getCollection("ingredients").drop();
        db.getCollection("products").drop();
    }
}
