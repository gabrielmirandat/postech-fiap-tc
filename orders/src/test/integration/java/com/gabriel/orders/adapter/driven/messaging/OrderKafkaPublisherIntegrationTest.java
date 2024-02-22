package com.gabriel.orders.adapter.driven.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.adapter.container.KafkaTestContainer;
import com.gabriel.orders.core.OrderMock;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.infra.kafka.KafkaConfig;
import com.gabriel.orders.infra.serializer.SerializerConfig;
import io.cloudevents.CloudEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import({KafkaConfig.class, SerializerConfig.class})
@ContextConfiguration(classes = {OrderKafkaPublisher.class, KafkaTestContainer.class})
public class OrderKafkaPublisherIntegrationTest {

    @Autowired
    ConsumerFactory<String, CloudEvent> consumerFactory;
    @Autowired
    private OrderKafkaPublisher orderKafkaPublisher;
    @Autowired
    private ObjectMapper objectMapper;
    private Order order;

    @BeforeAll
    public static void startContainer() {
        KafkaTestContainer.startContainer();
    }

    @AfterAll
    public static void stopContainer() {
        KafkaTestContainer.stopContainer();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        KafkaTestContainer.kafkaProperties(registry);
    }

    @BeforeEach
    void setup() {
        order = OrderMock.generateBasic();
    }

    @Test
    void testOrderCreatedEventIsPublished() throws Exception {
        orderKafkaPublisher.orderCreated(new OrderCreatedEvent(order));

        Consumer<String, CloudEvent> consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("orders"));

        ConsumerRecord<String, CloudEvent> record = KafkaTestUtils.getSingleRecord(consumer, "orders");

        assertThat(record).isNotNull();
        CloudEvent receivedEvent = record.value();
        assertThat(receivedEvent).isNotNull();

        byte[] data = Objects.requireNonNull(receivedEvent.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        Order receivedOrder = objectMapper.readValue(json, Order.class);
        assertEquals(receivedOrder.getOrderId().getId(), order.getOrderId().getId());
    }
}
