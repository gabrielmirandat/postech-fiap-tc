package com.gabriel.menu.infra.mongodb;

import com.mongodb.client.MongoClient;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;


@Liveness
public class MongoDBHealthCheck implements HealthCheck {

    @Inject
    MongoClient mongoClient;

    @Override
    public HealthCheckResponse call() {
        try {
            // Perform a simple operation, e.g., list database names
            mongoClient.listDatabaseNames().first();
            return HealthCheckResponse.up("MongoDB connection check");
        } catch (Exception e) {
            return HealthCheckResponse.down("MongoDB connection check");
        }
    }
}
