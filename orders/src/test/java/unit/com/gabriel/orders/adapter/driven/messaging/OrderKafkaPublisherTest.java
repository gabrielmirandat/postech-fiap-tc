package unit.com.gabriel.orders.adapter.driven.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.orders.adapter.driven.messaging.OrderKafkaPublisher;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;
import com.gabriel.orders.core.domain.event.OrderDeletedEvent;
import io.cloudevents.CloudEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import utils.com.gabriel.orders.core.domain.OrderMock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class OrderKafkaPublisherTest {

    private final String topicName = "test-topic";
    private final ObjectMapper objectMapper = objectMapper();
    @Mock
    private KafkaTemplate<String, CloudEvent> kafkaTemplate;
    private OrderKafkaPublisher orderKafkaPublisher;

    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Assuming the value is injected via constructor in the actual test class
        orderKafkaPublisher = new OrderKafkaPublisher(objectMapper, kafkaTemplate, topicName);
    }

    @Test
    void orderCreated_sendsMessageToKafka() throws Exception {
        // Given
        OrderCreatedEvent event = new OrderCreatedEvent(OrderMock.validBasicOrder());


        // When
        orderKafkaPublisher.orderCreated(event);

        // Then
        verify(kafkaTemplate).send(eq(topicName), any(CloudEvent.class));
    }

    @Test
    void orderCancelled_sendsMessageToKafka() throws Exception {
        // Given
        OrderDeletedEvent event = new OrderDeletedEvent(OrderMock.validBasicOrder().getTicketId());

        // When
        orderKafkaPublisher.orderCanceled(event);

        // Then
        verify(kafkaTemplate).send(eq(topicName), any(CloudEvent.class));
    }
}
