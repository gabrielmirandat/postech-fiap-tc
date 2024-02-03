package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.ValueObject;
import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.IngredientID;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;

@Getter
public class Extra extends ValueObject {

    private final IngredientID ingredientID;

    private final Name name;

    private final Price price;

    private Instant timestamp;

    @JsonCreator
    public Extra(@JsonProperty("ingredientID") IngredientID ingredientID,
                 @JsonProperty("name") Name name,
                 @JsonProperty("value") Price value,
                 @JsonProperty("timestamp") @JsonAlias("updateTimestamp") Instant timestamp) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.price = value;
        this.timestamp = timestamp;
    }

    public Extra(IngredientID ingredientID,
                 Name name,
                 Price value) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.price = value;
    }

    public Extra(IngredientID ingredientID, String name, Double value) {
        this.ingredientID = ingredientID;
        this.name = new Name(name);
        this.price = new Price(value);
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
}
