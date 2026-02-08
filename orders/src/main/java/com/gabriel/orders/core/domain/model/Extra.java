package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.IngredientId;
import com.gabriel.model.Model;

import java.io.IOException;
import java.time.Instant;

public class Extra {

    private final IngredientId ingredientId;

    private final Name name;

    private final Price price;

    private Instant timestamp;

    // Private constructor for internal use
    private Extra(IngredientId ingredientId, Name name, Price value, Instant timestamp) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.price = value;
        this.timestamp = timestamp;
    }

    // Factory methods que sempre validam
    public static Extra create(IngredientId ingredientId, String name, Double value) {
        IngredientId validatedIngredientId = (IngredientId) Model.validate(ingredientId);
        Name validatedName = (Name) Model.validate(Name.newBuilder().setValue(name).build());
        Price validatedPrice = (Price) Model.validate(Price.newBuilder().setValue(value).build());
        return new Extra(validatedIngredientId, validatedName, validatedPrice, null);
    }

    public static Extra create(IngredientId ingredientId, Name name, Price value) {
        Name validatedName = (Name) Model.validate(name);
        Price validatedPrice = (Price) Model.validate(value);
        return new Extra(ingredientId, validatedName, validatedPrice, null);
    }

    public static Extra create(IngredientId ingredientId, Name name, Price value, Instant timestamp) {
        Name validatedName = (Name) Model.validate(name);
        Price validatedPrice = (Price) Model.validate(value);
        return new Extra(ingredientId, validatedName, validatedPrice, timestamp);
    }

    // Constructor for Jackson deserialization (no validation to avoid duplication)
    @JsonCreator
    public static Extra fromJson(@JsonProperty("ingredientID") IngredientId ingredientId,
                                 @JsonProperty("name") Name name,
                                 @JsonProperty("value") Price value,
                                 @JsonProperty("timestamp") @JsonAlias("updateTimestamp") Instant timestamp) {
        return new Extra(ingredientId, name, value, timestamp);
    }

    public static Extra deserialize(ObjectMapper deserializer, byte[] bytes) {
        try {
            return deserializer.readValue(bytes, Extra.class);
        } catch (IOException e) {
            throw new IllegalStateException("Error deserializing extra");
        }
    }

    public byte[] serialized(ObjectMapper serializer) {
        try {
            return serializer.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error serializing extra: " + e.getMessage(), e);
        }
    }

    public IngredientId getIngredientId() {
        return ingredientId;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}