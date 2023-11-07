package com.gabriel.orders.adapter.driven.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.core.domain.events.MenuAddedEvent;
import com.gabriel.orders.core.domain.events.OrderCreatedEvent;
import com.gabriel.orders.core.domain.events.enums.Event;
import com.gabriel.orders.core.domain.events.enums.Topic;
import com.gabriel.orders.core.domain.ports.OrderPublisher;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.UUID;

@Service
public class OrderKafkaPublisher implements OrderPublisher {

    private final KafkaTemplate<String, CloudEvent> kafkaTemplate;

    private final ObjectMapper mapper;

    public OrderKafkaPublisher(KafkaTemplate<String, CloudEvent> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    @Override
    public void orderCreated(OrderCreatedEvent event) {

    }

    @Override
    public void productCreated(MenuAddedEvent event) {

        // Criar um evento CloudEvent
        CloudEvent data = CloudEventBuilder.v1()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create(event.source()))
                .withSubject(event.subject())
                .withType(event.type())
                .withData(PojoCloudEventData.wrap(event, mapper::writeValueAsBytes))
                .withExtension(Event.AUDIENCE.info(), Event.Audience.EXTERNAL_BOUNDED_CONTEXT.audienceName())
                .withExtension(Event.CONTEXT.info(), Event.Context.DOMAIN.eventContextName())
                .build();

        kafkaTemplate.send(Topic.MENU.topicName(), data);
    }
}
