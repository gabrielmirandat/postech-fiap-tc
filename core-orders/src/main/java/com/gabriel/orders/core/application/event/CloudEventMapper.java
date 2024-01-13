package com.gabriel.orders.core.application.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.common.core.domain.base.DomainEvent;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class CloudEventMapper {

    public static CloudEvent ceFrom(DomainEvent event) throws JsonProcessingException {
        return CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withSource(URI.create(event.source()))
            .withSubject(event.subject())
            .withType(event.type())
            .withData(event.payload())
            .withExtension("audience", event.audience())
            .withExtension("context", event.context())
            .build();
    }

    public static Product productFrom(CloudEvent event, ObjectMapper mapper) throws JsonProcessingException {
        byte[] data = Objects.requireNonNull(event.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        return mapper.readValue(json, Product.class);
    }

    public static Extra extraFrom(CloudEvent event, ObjectMapper mapper) throws JsonProcessingException {
        byte[] data = Objects.requireNonNull(event.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        return mapper.readValue(json, Extra.class);
    }
}
