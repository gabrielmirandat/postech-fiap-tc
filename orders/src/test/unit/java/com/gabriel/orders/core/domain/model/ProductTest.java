package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.ProductId;
import com.gabriel.model.Model;
import com.gabriel.orders.core.domain.model.Product;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        // Configure to ignore unknown properties (needed for Protobuf objects)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Configure to ignore properties that can't be serialized
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // Configure to ignore properties that cause serialization issues
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        return mapper;
    }

    @Test
    void shouldCreateProductSuccessfully_whenValidDataIsProvided() {
        // Arrange & Act
        Product product = Product.create(ProductId.newBuilder().setValue("12345678-PRDC-2024-12-20").build(), "Product", 2.0);

        // Assert
        assertThat(product).isNotNull();
        assertThat(product.getProductId()).isNotNull();
        assertThat(product.getName().getValue()).isEqualTo("Product");
        assertThat(product.getPrice().getValue()).isEqualTo(2.0);
    }

    @Test
    void shouldThrowException_whenProductIdIsInvalid() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> Product.create(ProductId.newBuilder().setValue("invalid-id-format").build(), "Product", 2.0))
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Validation error");
    }

    @Test
    void shouldThrowException_whenNameIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> Product.create(ProductId.newBuilder().setValue("product-123").build(), "", 2.0))
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Validation error");
    }

    @Test
    void shouldThrowException_whenNameIsEmpty() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> Model.validate(Name.newBuilder().setValue("").build()))
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Validation error");
    }

    @Test
    void shouldCreateProductSuccessfully_whenValidDataIsProvidedWithTimestamp() {
        // Arrange & Act
        Product product = Product.create(ProductId.newBuilder().setValue("product-123").build(), 
            Name.newBuilder().setValue("Product").build(), 
            Price.newBuilder().setValue(2.0).build(), Instant.now());

        // Assert
        assertThat(product).isNotNull();
        assertThat(product.getProductId()).isNotNull();
        assertThat(product.getName().getValue()).isEqualTo("Product");
        assertThat(product.getPrice().getValue()).isEqualTo(2.0);
        assertThat(product.getTimestamp()).isNotNull();
    }

    // @Test
    void shouldSerializeProductSuccessfully_whenValidDataIsProvided() {
        // Arrange
        Product product = Product.create(ProductId.newBuilder().setValue("12345678-PRDC-2024-12-20").build(), "Product", 2.0);

        // Act
        byte[] serialized = product.serialized(objectMapper());

        // Assert
        assertThat(serialized).isNotNull();
    }

    // @Test
    void shouldDeserializeProductSuccessfully_whenValidDataIsProvided() {
        // Arrange
        Product product = Product.create(ProductId.newBuilder().setValue("12345678-PRDC-2024-12-20").build(), "Product", 2.0);
        byte[] serialized = product.serialized(objectMapper());

        // Act
        Product deserialized = Product.deserialize(objectMapper(), serialized);

        // Assert
        assertThat(deserialized).isNotNull();
        assertThat(deserialized.getProductId()).isEqualTo(product.getProductId());
        assertThat(deserialized.getName().getValue()).isEqualTo("Product");
        assertThat(deserialized.getPrice().getValue()).isEqualTo(2.0);
    }
}
