package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.ProductID;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Test
    void shouldCreateProductSuccessfully_whenValidDataIsProvided() {
        // Arrange & Act
        Product product = new Product(new ProductID(), "Product", 2.0);

        // Assert
        assertThat(product).isNotNull();
        assertThat(product.getProductID()).isNotNull();
        assertThat(product.getName().getValue()).isEqualTo("Product");
        assertThat(product.getPrice().getValue()).isEqualTo(2.0);
    }

    // @Test
    void shouldThrowException_whenProductIDIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Product(null, "Product", 2.0))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Domain validation failed: value ProductID cannot be null");
    }

    @Test
    void shouldThrowException_whenNameIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Product(new ProductID(), null, 2.0))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Domain validation failed: value Name cannot be null or empty");
    }

    @Test
    void shouldThrowException_whenNameIsEmpty() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Product(new ProductID(), "", 2.0))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Domain validation failed: value Name cannot be null or empty");
    }

    @Test
    void shouldCreateProductSuccessfully_whenValidDataIsProvidedWithTimestamp() {
        // Arrange & Act
        Product product = new Product(new ProductID(), new Name("Product"), new Price(2.0), Instant.now());

        // Assert
        assertThat(product).isNotNull();
        assertThat(product.getProductID()).isNotNull();
        assertThat(product.getName().getValue()).isEqualTo("Product");
        assertThat(product.getPrice().getValue()).isEqualTo(2.0);
        assertThat(product.getTimestamp()).isNotNull();
    }

    @Test
    void shouldSerializeProductSuccessfully_whenValidDataIsProvided() {
        // Arrange
        Product product = new Product(new ProductID(), "Product", 2.0);

        // Act
        byte[] serialized = product.serialized(objectMapper());

        // Assert
        assertThat(serialized).isNotNull();
    }

    @Test
    void shouldDeserializeProductSuccessfully_whenValidDataIsProvided() {
        // Arrange
        Product product = new Product(new ProductID(), "Product", 2.0);
        byte[] serialized = product.serialized(objectMapper());

        // Act
        Product deserialized = Product.deserialize(objectMapper(), serialized);

        // Assert
        assertThat(deserialized).isNotNull();
        assertThat(deserialized.getProductID()).isEqualTo(product.getProductID());
        assertThat(deserialized.getName().getValue()).isEqualTo("Product");
        assertThat(deserialized.getPrice().getValue()).isEqualTo(2.0);
    }
}
