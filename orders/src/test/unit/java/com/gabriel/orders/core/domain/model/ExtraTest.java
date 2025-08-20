package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.IngredientId;
import com.gabriel.model.Model;
import com.gabriel.orders.core.domain.model.Extra;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExtraTest {

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
    void shouldCreateExtraSuccessfully_whenValidDataIsProvided() {
        // Arrange & Act
        Extra extra = Extra.create(IngredientId.newBuilder().setValue("12345678-INGR-2024-12-20").build(), "Extra", 2.0);

        // Assert
        assertThat(extra).isNotNull();
        assertThat(extra.getIngredientId()).isNotNull();
        assertThat(extra.getName().getValue()).isEqualTo("Extra");
        assertThat(extra.getPrice().getValue()).isEqualTo(2.0);
    }

    @Test
    void shouldThrowException_whenIngredientIDIsInvalid() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> Extra.create(IngredientId.newBuilder().setValue("invalid-id-format").build(), "Extra", 2.0))
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Validation error");
    }

    @Test
    void shouldThrowException_whenNameIsNull() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> Extra.create(IngredientId.newBuilder().setValue("ingredient-123").build(), "", 2.0))
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
    void shouldCreateExtraSuccessfully_whenValidDataIsProvidedWithTimestamp() {
        // Arrange & Act
        Extra extra = Extra.create(IngredientId.newBuilder().setValue("12345678-INGR-2024-12-20").build(), 
            Name.newBuilder().setValue("Extra").build(), 
            Price.newBuilder().setValue(2.0).build(), Instant.now());

        // Assert
        assertThat(extra).isNotNull();
        assertThat(extra.getIngredientId()).isNotNull();
        assertThat(extra.getName().getValue()).isEqualTo("Extra");
        assertThat(extra.getPrice().getValue()).isEqualTo(2.0);
        assertThat(extra.getTimestamp()).isNotNull();
    }

    // @Test
    void shouldSerializeExtraSuccessfully_whenValidDataIsProvided() {
        // Arrange
        Extra extra = Extra.create(IngredientId.newBuilder().setValue("12345678-INGR-2024-12-20").build(), 
            Name.newBuilder().setValue("Extra").build(), 
            Price.newBuilder().setValue(2.0).build(), Instant.now());

        // Act
        byte[] serialized = extra.serialized(objectMapper());

        // Assert
        assertThat(serialized).isNotNull();
    }

    // @Test
    void shouldDeserializeExtraSuccessfully_whenValidDataIsProvided() {
        // Arrange
        Extra extra = Extra.create(IngredientId.newBuilder().setValue("12345678-INGR-2024-12-20").build(), 
            Name.newBuilder().setValue("Extra").build(), 
            Price.newBuilder().setValue(2.0).build(), Instant.now());
        byte[] serialized = extra.serialized(objectMapper());

        // Act
        Extra deserialized = Extra.deserialize(objectMapper(), serialized);

        // Assert
        assertThat(deserialized).isNotNull();
        assertThat(deserialized.getIngredientId()).isEqualTo(extra.getIngredientId());
        assertThat(deserialized.getName().getValue()).isEqualTo("Extra");
        assertThat(deserialized.getPrice().getValue()).isEqualTo(2.0);
        assertThat(deserialized.getTimestamp()).isEqualTo(extra.getTimestamp());
    }
}
