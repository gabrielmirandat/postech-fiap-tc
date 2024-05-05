package com.gabriel.orders.infra.application;

import com.gabriel.orders.adapter.driven.api.MenuGrpcClient;
import com.gabriel.orders.adapter.driven.api.PermissionGrpcClient;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class StartManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartManager.class);
    private final MenuGrpcClient menuGrpcClient;

    private final PermissionGrpcClient permissionGrpcClient;
    List<String> allowedAttributes = Arrays.asList("mongo", "liquibase", "kafka", "redis", "newrelic", "grpc");
    List<String> notAllowedAttributes = Arrays.asList("credential", "password");
    Environment environment;

    public StartManager(Environment environment, MenuGrpcClient menuGrpcClient,
                        PermissionGrpcClient permissionGrpcClient) {
        this.environment = environment;
        this.menuGrpcClient = menuGrpcClient;
        this.permissionGrpcClient = permissionGrpcClient;
    }

    @PostConstruct
    public void exec() {
        showProperties();
        menuGrpcClient.dumpMenuData();
        permissionGrpcClient.dumpPermissionData();
    }

    public void showProperties() {
        LOGGER.info("\n\n====== Environment and configuration ======");
        LOGGER.info("Active profiles: {}", Arrays.toString(environment.getActiveProfiles()));
        final MutablePropertySources sources = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(sources.spliterator(), false)
            .filter(ps -> ps instanceof EnumerablePropertySource)
            .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
            .flatMap(Arrays::stream)
            .distinct()
            .filter(prop -> allowedAttributes.stream().anyMatch(prop::contains) && notAllowedAttributes.stream().noneMatch(prop::contains))
            .sorted()
            .forEach(prop -> LOGGER.info("{}: {}", prop, environment.getProperty(prop)));
        LOGGER.info("===========================================\n");
    }
}
