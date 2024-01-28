package com.gabriel.menu.infra.grpc;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
public class GrpcHealthCheck implements HealthCheck {

    // Inject your gRPC service or other relevant components

    @Override
    public HealthCheckResponse call() {
        try {
            // Perform a simple operation, e.g., list database names
            return HealthCheckResponse.up("GRPC connection check");
        } catch (Exception e) {
            return HealthCheckResponse.down("GRPC connection check");
        }
    }
}

