package com.gabriel.menu.infra.application;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@ApplicationScoped
public class StartManager {

    private static final Logger LOGGER = Logger.getLogger(StartManager.class);
    private List<String> allowedAttributes = Arrays.asList("mongo", "liquibase", "kafka", "redis", "newrelic", "grpc");
    private List<String> notAllowedAttributes = Arrays.asList("credential", "password");

    void onStart(@Observes StartupEvent ev) {
        showProperties();
        // You can perform other initialization tasks here
    }

    public void showProperties() {
        LOGGER.info("\n\n====== Environment and configuration ======");
        LOGGER.info("Active profiles: " + ProfileManager.getActiveProfile());

        Config config = ConfigProvider.getConfig();

        SortedSet<String> sortedPropertyNames = new TreeSet<>();
        for (String propertyName : config.getPropertyNames()) {
            if (allowedAttributes.stream().anyMatch(propertyName::contains) &&
                notAllowedAttributes.stream().noneMatch(propertyName::contains)) {
                sortedPropertyNames.add(propertyName);
            }
        }

        for (String propertyName : sortedPropertyNames) {
            LOGGER.info(propertyName + ": " + config.getValue(propertyName, String.class));
        }

        LOGGER.info("===========================================\n");
    }
}
