package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.IngredientId;

import java.io.IOException;
import java.time.Instant;

public class Ingredient extends Menu {

    private final IngredientId ingredientID;

    private final Name name;

    private final Price price;

    private final Category category;

    private final Weight weight;

    private final boolean isExtra;

    public Ingredient(Name name, Category category, Price price, Weight weight, boolean isExtra) {
        this.ingredientID = IngredientId.newBuilder().build();
        this.name = name;
        this.category = category;
        this.price = price;
        this.weight = weight;
        this.isExtra = isExtra;
    }

    public Ingredient(String name, Category category, Double price, Double weight, boolean isExtra) {
        this.ingredientID = IngredientId.newBuilder().build();
        this.name = Name.newBuilder().setValue(name).build();
        this.category = category;
        this.price = Price.newBuilder().setValue(price).build();
        this.weight = new Weight(weight);
        this.isExtra = isExtra;
    }

    /**
     * Constructor for Jackson deserialization.
     */
    @JsonCreator
    Ingredient(@JsonProperty("menuId") String menuId, @JsonProperty("name") String nameStr,
               @JsonProperty("category") Category category, @JsonProperty("price") Double priceValue,
               @JsonProperty("weight") Weight weight, @JsonProperty("extra") boolean isExtra,
               @JsonProperty("createdAt") Instant createdAt, @JsonProperty("updatedAt") Instant updatedAt) {
        this.ingredientID = IngredientId.newBuilder().setValue(menuId).build();
        this.name = Name.newBuilder().setValue(nameStr).build();
        this.category = category;
        this.price = Price.newBuilder().setValue(priceValue).build();
        this.weight = weight;
        this.isExtra = isExtra;
        this.creationTimestamp = createdAt;
        this.updateTimestamp = updatedAt;
    }

    public static Ingredient copy(IngredientId ingredientID, Name name, Category category, Price price,
                                  Weight weight, boolean isExtra, Instant createdAt, Instant updatedAt) {
        return new Ingredient(ingredientID.getValue(), name.getValue(), category, price.getValue(), weight, isExtra, createdAt, updatedAt);
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
            throw new IllegalStateException("Error serializing ingredient: " + e.getMessage(), e);
        }
    }

    @Override
    public String getMenuId() {
        return ingredientID.getValue();
    }

    @JsonIgnore
    public IngredientId getIngredientID() {
        return ingredientID;
    }
    
    @JsonProperty("menuId")
    public String getMenuIdString() {
        return ingredientID.getValue();
    }

    @JsonIgnore
    public Name getName() {
        return name;
    }
    
    @JsonProperty("name")
    public String getNameString() {
        return name.getValue();
    }

    @JsonIgnore
    public Price getPrice() {
        return price;
    }
    
    @JsonProperty("price")
    public Double getPriceValue() {
        return price.getValue();
    }

    public Category getCategory() {
        return category;
    }

    public Weight getWeight() {
        return weight;
    }

    public boolean isExtra() {
        return isExtra;
    }
}
