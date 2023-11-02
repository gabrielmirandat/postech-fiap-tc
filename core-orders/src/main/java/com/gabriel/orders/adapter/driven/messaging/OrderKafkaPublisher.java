package com.gabriel.orders.adapter.driven.messaging;

import com.gabriel.orders.core.domain.events.MenuAddedEvent;
import com.gabriel.orders.core.domain.events.OrderCreatedEvent;
import com.gabriel.orders.core.domain.ports.OrderPublisher;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class OrderKafkaPublisher implements OrderPublisher {

    private final KafkaTemplate<String, CloudEvent> kafkaTemplate;

    public OrderKafkaPublisher(KafkaTemplate<String, CloudEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void orderCreated(OrderCreatedEvent event) {

    }

    @Override
    public void productCreated(MenuAddedEvent event) {

        // Criar um evento CloudEvent
        CloudEvent data = CloudEventBuilder.v1()
                .withId("123")
                .withType("menu.product.added")
                .withSource(URI.create("your-source"))
                .withData("{\"productID\":\"123\",\"name\":\"Pizza\",\"value\":\"10.99\"}".getBytes(StandardCharsets.UTF_8))
                .build();

        kafkaTemplate.send("menub", data);
    }
}
