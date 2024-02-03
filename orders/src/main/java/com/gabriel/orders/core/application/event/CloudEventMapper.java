package com.gabriel.orders.core.application.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.DomainEvent;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class CloudEventMapper {

    public static CloudEvent ceFrom(ObjectMapper serializer, DomainEvent event) throws JsonProcessingException {
        return CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withSource(URI.create(event.source()))
            .withSubject(event.subject())
            .withType(event.type())
            .withData(event.payload(serializer))
            .withExtension("audience", event.audience())
            .withExtension("context", event.context())
            .build();
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
