package com.gabriel.orders.adapter.driver.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.adapter.container.KafkaTestContainer;
import com.gabriel.orders.core.OrderMock;
import com.gabriel.orders.core.application.usecase.UpdateMenuUseCase;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.infra.kafka.KafkaConfig;
import com.gabriel.orders.infra.serializer.SerializerConfig;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Import({KafkaConfig.class, SerializerConfig.class})
@ContextConfiguration(classes = {MenuKafkaSubscriber.class, KafkaTestContainer.class})
public class MenuKafkaSubscriberTest {

    @SpyBean
    UpdateMenuUseCase updateMenuUseCase;
    @Autowired
    private KafkaTemplate<String, CloudEvent> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuRepository menuRepository; // Mocking the repository instead of the use case
    @Captor
    private ArgumentCaptor<Product> productCaptor;
    @Captor
    private ArgumentCaptor<Extra> extraCaptor;

    private Product product;

    private Extra extra;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        KafkaTestContainer.kafkaProperties(registry);
    }

    @BeforeEach
    void setup() {
        product = OrderMock.generateProduct();
        extra = OrderMock.generateExtra();
    }

    @Test
    void testProductAddedEventIsProcessed() throws Exception {
        String productJson = objectMapper.writeValueAsString(product);

        // Create a CloudEvent for the product added event
        CloudEvent cloudEvent = CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withType("postech.menu.v1.product.created")
            .withSource(URI.create("/menu"))
            .withData(productJson.getBytes(StandardCharsets.UTF_8))
            .withTime(OffsetDateTime.now())
            .build();

        kafkaTemplate.send("menu", cloudEvent);

        // Use verify with timeout to wait for event processed
        verify(updateMenuUseCase, timeout(10000)).handleProductAdded(any(Product.class));

        // Capture and assert the response passed to setupMenuUseCase
        verify(updateMenuUseCase).handleProductAdded(productCaptor.capture());
        Product addedProduct = productCaptor.getValue();

        // Verify that the repository method to add a product was called
        verify(menuRepository).addProduct(addedProduct);

        // Compare addedProduct with the product sent in the event
        assertEquals(product.getProductID().getId(), addedProduct.getProductID().getId());
        assertEquals(product.getName().getValue(), addedProduct.getName().getValue());
        assertEquals(product.getPrice().getValue(), addedProduct.getPrice().getValue());
        assertEquals(product.getTimestamp(), addedProduct.getTimestamp());
    }

    @Test
    void testProductDeletedEventIsProcessed() throws Exception {
        String productJson = objectMapper.writeValueAsString(product);

        CloudEvent cloudEvent = CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withType("postech.menu.v1.product.deleted")
            .withSource(URI.create("/menu"))
            .withData(productJson.getBytes(StandardCharsets.UTF_8))
            .withTime(OffsetDateTime.now())
            .build();

        kafkaTemplate.send("menu", cloudEvent);

        // Use verify with timeout to wait for event processed
        verify(updateMenuUseCase, timeout(10000)).handleProductDeleted(any(Product.class));

        verify(updateMenuUseCase).handleProductDeleted(productCaptor.capture());
        Product deletedProduct = productCaptor.getValue();

        verify(menuRepository).deleteProduct(deletedProduct.getProductID());

        assertEquals(product.getProductID().getId(), deletedProduct.getProductID().getId());
    }

    @Test
    void testExtraAddedEventIsProcessed() throws Exception {
        String extraJson = objectMapper.writeValueAsString(extra);

        CloudEvent cloudEvent = CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withType("postech.menu.v1.ingredient.created")
            .withSource(URI.create("/menu"))
            .withData(extraJson.getBytes(StandardCharsets.UTF_8))
            .withTime(OffsetDateTime.now())
            .build();

        kafkaTemplate.send("menu", cloudEvent);

        // Use verify with timeout to wait for event processed
        verify(updateMenuUseCase, timeout(10000)).handleExtraAdded(any(Extra.class));

        verify(updateMenuUseCase).handleExtraAdded(extraCaptor.capture());
        Extra addedExtra = extraCaptor.getValue();

        verify(menuRepository).addExtra(addedExtra);

        assertEquals(extra.getIngredientID().getId(), addedExtra.getIngredientID().getId());
    }

    @Test
    void testExtraDeletedEventIsProcessed() throws Exception {
        String extraJson = objectMapper.writeValueAsString(extra);

        CloudEvent cloudEvent = CloudEventBuilder.v1()
            .withId(UUID.randomUUID().toString())
            .withType("postech.menu.v1.ingredient.deleted")
            .withSource(URI.create("/menu"))
            .withData(extraJson.getBytes(StandardCharsets.UTF_8))
            .withTime(OffsetDateTime.now())
            .build();

        kafkaTemplate.send("menu", cloudEvent);

        // Use verify with timeout to wait for event processed
        verify(updateMenuUseCase, timeout(10000)).handleExtraDeleted(any(Extra.class));

        verify(updateMenuUseCase).handleExtraDeleted(extraCaptor.capture());
        Extra deletedExtra = extraCaptor.getValue();

        verify(menuRepository).deleteExtra(deletedExtra.getIngredientID());

        assertEquals(extra.getIngredientID().getId(), deletedExtra.getIngredientID().getId());
    }
}

