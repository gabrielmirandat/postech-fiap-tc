package com.gabriel.orders.infra.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuGrpcClientConfig {

    @Value("${grpc.menu.server.host}")
    private String grpcMenuServerHost;

    @Value("${grpc.menu.server.port}")
    private int grpcMenuServerPort;

    @Bean(name = "menuManagedChannel")
    public ManagedChannel menuManagedChannel() {
        return ManagedChannelBuilder
            .forAddress(grpcMenuServerHost, grpcMenuServerPort)
            .usePlaintext()
            .build();
    }

    @PreDestroy
    public void destroyMenuChannel(@Qualifier("menuManagedChannel") ManagedChannel managedMenuChannel) {
        if (managedMenuChannel != null && !managedMenuChannel.isShutdown()) {
            managedMenuChannel.shutdown();
        }
    }
}
