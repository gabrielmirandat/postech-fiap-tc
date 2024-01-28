package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.domain.model.Name;
import com.gabriel.domain.model.Price;
import com.gabriel.domain.model.id.IngredientID;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;

@Getter
public class Ingredient extends Menu {

    private final IngredientID ingredientID;

    private final Name name;

    private final Price price;

    private final Category category;

    private final Weight weight;

    private final boolean isExtra;

    public Ingredient(Name name, Category category, Price price, Weight weight, boolean isExtra) {
        this.ingredientID = new IngredientID();
        this.name = name;
        this.category = category;
        this.price = price;
        this.weight = weight;
        this.isExtra = isExtra;
    }

    public Ingredient(String name, Category category, Double price, Double weight, boolean isExtra) {
        this.ingredientID = new IngredientID();
        this.name = new Name(name);
        this.category = category;
        this.price = new Price(price);
        this.weight = new Weight(weight);
        this.isExtra = isExtra;
    }

    private Ingredient(IngredientID ingredientID, Name name, Category category, Price price,
                       Weight weight, boolean isExtra, Instant createdAt, Instant updatedAt) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.weight = weight;
        this.isExtra = isExtra;
        this.creationTimestamp = createdAt;
        this.updateTimestamp = updatedAt;
    }

    public static Ingredient copy(IngredientID ingredientID, Name name, Category category, Price price,
                                  Weight weight, boolean isExtra, Instant createdAt, Instant updatedAt) {
        return new Ingredient(ingredientID, name, category, price, weight, isExtra, createdAt, updatedAt);
    }

    public static Ingredient deserialize(ObjectMapper deserializer, byte[] bytes) {
        try {
            return deserializer.readValue(bytes, Ingredient.class);
        } catch (IOException e) {
            throw new IllegalStateException("Error deserializing ingredient");
        }
    }

    public byte[] serialized(ObjectMapper serializer) {
        try {
            return serializer.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error serializing ingredient");
        }
    }

    @Override
    public String getMenuId() {
        return ingredientID.getId();
    }
}
