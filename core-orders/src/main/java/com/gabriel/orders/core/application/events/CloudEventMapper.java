package com.gabriel.orders.core.application.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.core.domain.base.DomainEvent;
import com.gabriel.orders.core.domain.valueobjects.Extra;
import com.gabriel.orders.core.domain.valueobjects.Product;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.CloudEventUtils;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.jackson.PojoCloudEventDataMapper;

import java.net.URI;
import java.util.UUID;

public class CloudEventMapper {

    public static CloudEvent ceFrom(DomainEvent event, ObjectMapper mapper) {
        return CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withSource(URI.create(event.source()))
            .withSubject(event.subject())
            .withType(event.type())
            .withData(PojoCloudEventData.wrap(event, mapper::writeValueAsBytes))
            .withExtension("audience", event.audience())
            .withExtension("context", event.context())
            .build();
    }

    public static Product productFrom(CloudEvent event, ObjectMapper mapper) {
        return CloudEventUtils.mapData(event, PojoCloudEventDataMapper
            .from(mapper, Product.class)).getValue();
    }

    public static Extra extraFrom(CloudEvent event, ObjectMapper mapper) {
        return CloudEventUtils.mapData(event, PojoCloudEventDataMapper
            .from(mapper, Extra.class)).getValue();
    }
}
