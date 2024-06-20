package integration.com.gabriel.orders.adapter.driven.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.adapter.driven.messaging.OrderKafkaPublisher;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;
import com.gabriel.orders.core.domain.event.OrderDeletedEvent;
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
import utils.com.gabriel.orders.adapter.container.KafkaTestContainer;
import utils.com.gabriel.orders.core.domain.OrderMock;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import({KafkaConfig.class, SerializerConfig.class})
@ContextConfiguration(classes = {OrderKafkaPublisher.class, KafkaTestContainer.class})
public class OrderKafkaPublisherIntegrationTest {

    private static Consumer<String, CloudEvent> consumer;
    @Autowired
    private OrderKafkaPublisher orderKafkaPublisher;
    @Autowired
    private ObjectMapper objectMapper;
    private Order order;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        KafkaTestContainer.kafkaProperties(registry);
    }

    @BeforeAll
    public static void setup(@Autowired ConsumerFactory<String, CloudEvent> consumerFactory) {
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("orders"));
    }

    @AfterAll
    public static void collect() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @BeforeEach
    void setup() {
        order = OrderMock.validBasicOrder();
    }

    @Test
    void testOrderCreatedEventIsPublished() throws Exception {
        orderKafkaPublisher.orderCreated(new OrderCreatedEvent(order));

        ConsumerRecord<String, CloudEvent> record = KafkaTestUtils.getSingleRecord(consumer, "orders");

        assertThat(record).isNotNull();
        CloudEvent receivedEvent = record.value();
        assertThat(receivedEvent).isNotNull();

        byte[] data = Objects.requireNonNull(receivedEvent.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        Order receivedOrder = objectMapper.readValue(json, Order.class);
        assertEquals(receivedOrder.getOrderId().getId(), order.getOrderId().getId());
    }

    @Test
    void testOrderCanceledEventIsPublished() throws Exception {
        orderKafkaPublisher.orderCanceled(new OrderDeletedEvent(order.getTicketId()));

        ConsumerRecord<String, CloudEvent> record = KafkaTestUtils.getSingleRecord(consumer, "orders");

        assertThat(record).isNotNull();
        CloudEvent receivedEvent = record.value();
        assertThat(receivedEvent).isNotNull();

        byte[] data = Objects.requireNonNull(receivedEvent.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        String receivedOrder = objectMapper.readValue(json, String.class);
        assertEquals(receivedOrder, order.getTicketId());
    }
}
