package com.gabriel.menu.adapter.driven.messaging.mapper;


import com.gabriel.core.domain.DomainEvent;
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
