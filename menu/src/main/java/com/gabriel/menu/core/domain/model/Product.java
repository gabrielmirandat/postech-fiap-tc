package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.domain.AggregateRoot;
import com.gabriel.domain.model.Name;
import com.gabriel.domain.model.Price;
import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.domain.model.id.ProductID;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

@Getter
public class Product extends AggregateRoot {

    private final ProductID productID;

    private final Name name;

    private final Price price;

    private final Category category;

    private final Description description;

    private final Image image;

    private final List<IngredientID> ingredients;

    @JsonCreator
    public Product(ProductID productID, Name name, Price price, Category category,
                   Description description, Image image, List<IngredientID> ingredients) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
    }

    public Product(ProductID productID, String name, Double price, Category category,
                   String description, String image, List<IngredientID> ingredients) {
        this.productID = productID;
        this.name = new Name(name);
        this.price = new Price(price);
        this.category = category;
        this.description = new Description(description);
        this.image = new Image(image);
        this.ingredients = ingredients;
    }

    public Product(String name, Double price, Category category,
                   String description, String image, List<IngredientID> ingredients) {
        this.productID = new ProductID();
        this.name = new Name(name);
        this.price = new Price(price);
        this.category = category;
        this.description = new Description(description);
        this.image = new Image(image);
        this.ingredients = ingredients;
    }

    public static Product deserialize(byte[] bytes) {
        try {
            return new ObjectMapper().readValue(bytes, Product.class);
        } catch (IOException e) {
            throw new IllegalStateException("Error deserializing product");
        }
    }

    public byte[] serialized() {
        try {
            return new ObjectMapper().writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error serializing product");
        }
    }
}
