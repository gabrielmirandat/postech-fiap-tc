package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.common.core.domain.base.ValueObject;
import com.gabriel.common.core.domain.model.Name;
import com.gabriel.common.core.domain.model.Price;
import com.gabriel.common.core.domain.model.id.IngredientID;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Extra extends ValueObject {

    private final IngredientID ingredientID;

    private final Name name;

    private final Price price;

    @JsonCreator
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

    public static Extra deserialize(byte[] bytes) {
        try {
            return new ObjectMapper().readValue(bytes, Extra.class);
        } catch (IOException e) {
            throw new IllegalStateException("Error deserializing extra");
        }
    }

    public byte[] serialized() {
        try {
            return new ObjectMapper().writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error serializing extra");
        }
    }
}
