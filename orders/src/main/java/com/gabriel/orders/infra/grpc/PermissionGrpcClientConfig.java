package com.gabriel.orders.infra.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermissionGrpcClientConfig {

    @Value("${grpc.permission.server.host}")
    private String grpcPermissionServerHost;

    @Value("${grpc.permission.server.port}")
    private int grpcPermissionServerPort;

    @Bean(name = "permissionManagedChannel")
    public ManagedChannel permissionManagedChannel() {
        return ManagedChannelBuilder
            .forAddress(grpcPermissionServerHost, grpcPermissionServerPort)
            .usePlaintext()
            .build();
    }

    @PreDestroy
    public void destroyPermissionChannel() {
        ManagedChannel permissionManagedChannel = permissionManagedChannel();
        if (permissionManagedChannel != null && !permissionManagedChannel.isShutdown()) {
            permissionManagedChannel.shutdown();
        }
    }

}
