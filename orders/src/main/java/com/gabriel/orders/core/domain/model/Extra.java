package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.IngredientId;

import java.io.IOException;
import java.time.Instant;

public class Extra {

    private final IngredientId ingredientId;

    private final Name name;

    private final Price price;

    private Instant timestamp;

    @JsonCreator
    public Extra(@JsonProperty("ingredientID") IngredientId ingredientId,
                 @JsonProperty("name") Name name,
                 @JsonProperty("value") Price value,
                 @JsonProperty("timestamp") @JsonAlias("updateTimestamp") Instant timestamp) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.price = value;
        this.timestamp = timestamp;
    }

    public Extra(IngredientId ingredientId,
                 Name name,
                 Price value) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.price = value;
    }

    public Extra(IngredientId ingredientId, String name, Double value) {
        this.ingredientId = ingredientId;
        this.name = Name.newBuilder().setValue(name).build();
        this.price = Price.newBuilder().setValue(value).build();
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
            throw new IllegalStateException("Error serializing extra");
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