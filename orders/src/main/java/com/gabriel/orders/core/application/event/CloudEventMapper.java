package com.gabriel.orders.core.application.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

// Simple interface for domain events
interface DomainEvent {
    String source();
    String subject();
    String type();
    byte[] payload(ObjectMapper serializer) throws JsonProcessingException;
    String audience();
    String context();
}

public class CloudEventMapper {

    public static CloudEvent ceFrom(ObjectMapper serializer, DomainEvent event) throws JsonProcessingException {
        CloudEventBuilder builder = CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withSource(URI.create(event.source()))
            .withSubject(event.subject())
            .withType(event.type())
            .withData(event.payload(serializer));
        
        // Only add extensions if they are not null
        if (event.audience() != null) {
            builder.withExtension("audience", event.audience());
        }
        if (event.context() != null) {
            builder.withExtension("context", event.context());
        }
        
        return builder.build();
    }

    public static CloudEvent ceFrom(ObjectMapper serializer, com.gabriel.orders.core.domain.event.OrderCreatedEvent e) throws JsonProcessingException {
        DomainEvent d = new DomainEvent() {
            public String source() { return e.source(); }
            public String subject() { return e.subject(); }
            public String type() { return e.type(); }
            public byte[] payload(ObjectMapper s) throws JsonProcessingException { return e.payload(s); }
            public String audience() { return null; }
            public String context() { return null; }
        };
        return ceFrom(serializer, d);
    }

    public static CloudEvent ceFrom(ObjectMapper serializer, com.gabriel.orders.core.domain.event.OrderDeletedEvent e) throws JsonProcessingException {
        DomainEvent d = new DomainEvent() {
            public String source() { return e.source(); }
            public String subject() { return e.subject(); }
            public String type() { return e.type(); }
            public byte[] payload(ObjectMapper s) throws JsonProcessingException { return e.payload(s); }
            public String audience() { return null; }
            public String context() { return null; }
        };
        return ceFrom(serializer, d);
    }

    public static Product productFrom(ObjectMapper deserializer, CloudEvent event) throws JsonProcessingException {
        byte[] data = Objects.requireNonNull(event.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        return deserializer.readValue(json, Product.class);
    }

    public static Extra extraFrom(ObjectMapper deserializer, CloudEvent event) throws JsonProcessingException {
        byte[] data = Objects.requireNonNull(event.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        return deserializer.readValue(json, Extra.class);
    }
}
