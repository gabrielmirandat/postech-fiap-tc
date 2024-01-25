package com.gabriel.orders.infra.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.menu.server.host}")
    private String grpcMenuServerHost;

    @Value("${grpc.menu.server.port}")
    private int grpcMenuServerPort;

    @Bean
    public ManagedChannel managedMenuChannel() {
        return ManagedChannelBuilder.forAddress(grpcMenuServerHost, grpcMenuServerPort)
            .usePlaintext()
            .build();
    }

    @PreDestroy
    public void destroyMenuChannel(ManagedChannel managedMenuChannel) {
        if (managedMenuChannel != null) {
            managedMenuChannel.shutdown();
        }
    }
}
