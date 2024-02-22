package com.gabriel.orders.adapter.driver.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.orders.core.application.usecase.UpdateMenuUseCase;
import com.gabriel.orders.core.domain.model.Product;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class MenuKafkaSubscriberTest {

    @Mock
    private UpdateMenuUseCase updateMenuUseCase;

    private MenuKafkaSubscriber subscriber;

    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @BeforeEach
    public void setUp() {
        openMocks(this);
        subscriber = new MenuKafkaSubscriber(new ObjectMapper(), updateMenuUseCase);
    }

    // Test methods will be added here
    @Test
    void whenProductAddedEvent_thenHandleProductAddedIsCalled() throws Exception {
        // Construct a CloudEvent for a product added
        CloudEvent cloudEvent = CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withType("postech.menu.v1.product.created")
            .withSource(URI.create("/menu"))
            .withData("{\"id\":\"prod-123\", \"name\":\"Pizza\"}" .getBytes())
            .withDataContentType("application/json")
            .withTime(OffsetDateTime.now())
            .build();

        // Mock ObjectMapper to simulate the extraction of a product from the CloudEvent
        // Assume Product is a class representing your product, and it's correctly instantiated from the CloudEvent's data
        // Mockito.when(objectMapper.readValue(any(byte[].class), eq(Product.class))).thenReturn(new Product(...));

        // Call the listener method directly
        subscriber.listenMenuEvents(cloudEvent, System.currentTimeMillis());

        // Verify that the appropriate method in the use case is called
        verify(updateMenuUseCase).handleProductAdded(any(Product.class));
    }


}
