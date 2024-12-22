package com.gabriel.orders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

@Component
public class ExternalPropertiesLoader {

    private final ConfigurableEnvironment environment;

    @Value("${external.properties.path:}")
    private String propertiesFilePath;

    public ExternalPropertiesLoader(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void loadExternalProperties() throws IOException {
        if (propertiesFilePath == null || propertiesFilePath.isBlank()) {
            System.out.println("DEBUG: No external properties file specified. Skipping external configuration.");
            return;
        }

        System.out.println("DEBUG: Attempting to load external properties from: " + propertiesFilePath);

        Properties properties = new Properties();
        try {
            properties.load(new PathResource(propertiesFilePath).getInputStream());
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load external properties file from: " + propertiesFilePath);
            throw e;
        }

        System.out.println("DEBUG: External properties loaded:");
        properties.forEach((key, value) -> {
            String rawValue = value.toString();
            String resolvedValue = resolvePlaceholders(rawValue);

            System.out.println("Key: " + key);
            System.out.println("  Raw value: " + rawValue);
            System.out.println("  Resolved value: " + resolvedValue);

            environment.getSystemProperties().put(key.toString(), resolvedValue);
        });
    }

    private String resolvePlaceholders(String value) {
        return environment.resolvePlaceholders(value);
    }
}
