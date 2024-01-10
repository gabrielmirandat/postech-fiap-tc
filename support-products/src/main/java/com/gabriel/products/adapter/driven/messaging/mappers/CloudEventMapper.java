package com.gabriel.products.adapter.driven.messaging.mappers;

import com.gabriel.products.core.domain.base.DomainEvent;
import io.smallrye.reactive.messaging.ce.OutgoingCloudEventMetadata;

import java.net.URI;

public class CloudEventMapper {

    public static OutgoingCloudEventMetadata<Object> fromEvent(DomainEvent event) {
        return OutgoingCloudEventMetadata.builder()
            .withSource(URI.create(event.source()))
            .withType(event.type())
            .withSubject(event.subject())
            .withExtension("audience", event.audience())
            .withExtension("context", event.context())
            .build();
    }
}
