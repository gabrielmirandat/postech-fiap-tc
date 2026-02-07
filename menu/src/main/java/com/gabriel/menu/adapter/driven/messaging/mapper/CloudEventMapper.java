package com.gabriel.menu.adapter.driven.messaging.mapper;


import com.gabriel.menu.core.domain.event.IngredientCreatedEvent;
import com.gabriel.menu.core.domain.event.ProductCreatedEvent;
import io.smallrye.reactive.messaging.ce.OutgoingCloudEventMetadata;

import java.net.URI;

public class CloudEventMapper {

    public static OutgoingCloudEventMetadata<Object> fromIngredientEvent(IngredientCreatedEvent event) {
        return OutgoingCloudEventMetadata.builder()
            .withSource(URI.create(event.source()))
            .withType(event.type())
            .withSubject(event.subject())
            .withExtension("audience", event.audience())
            .withExtension("context", event.context())
            .build();
    }

    public static OutgoingCloudEventMetadata<Object> fromProductEvent(ProductCreatedEvent event) {
        return OutgoingCloudEventMetadata.builder()
            .withSource(URI.create(event.source()))
            .withType(event.type())
            .withSubject(event.subject())
            .withExtension("audience", event.audience())
            .withExtension("context", event.context())
            .build();
    }
}
