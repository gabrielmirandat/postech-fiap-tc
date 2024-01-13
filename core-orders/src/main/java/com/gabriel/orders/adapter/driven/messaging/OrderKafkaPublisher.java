package com.gabriel.orders.adapter.driven.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.orders.core.application.event.CloudEventMapper;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import io.cloudevents.CloudEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaPublisher implements OrderPublisher {

    private final KafkaTemplate<String, CloudEvent> kafkaTemplate;

    private final String topicName;

    public OrderKafkaPublisher(KafkaTemplate<String, CloudEvent> kafkaTemplate,
                               @Value("${kafka.domain.topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void orderCreated(OrderCreatedEvent event) throws JsonProcessingException {
        kafkaTemplate.send(topicName, CloudEventMapper.ceFrom(event));
    }
}
