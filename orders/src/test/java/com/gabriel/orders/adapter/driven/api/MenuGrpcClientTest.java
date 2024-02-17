package com.gabriel.orders.adapter.driven.api;

import com.gabriel.orders.adapter.container.GrpcServerTestContainer;
import com.gabriel.orders.infra.grpc.GrpcClientConfig;
import io.grpc.ManagedChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@Import({GrpcClientConfig.class})
@ContextConfiguration(classes = {MenuGrpcClient.class, GrpcServerTestContainer.class})
public class MenuGrpcClientTest {

    @Autowired
    ManagedChannel managedMenuChannel;
    @Autowired
    MenuGrpcClient menuGrpcClient;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        GrpcServerTestContainer.grpcProperties(registry);
    }

    @Test
    public void testDumpMenuData() {
        menuGrpcClient.dumpMenuData();
    }
}

