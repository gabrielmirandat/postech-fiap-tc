package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.application.exception.ApplicationError;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.menu.core.domain.exception.MenuDomainError;
import com.gabriel.menu.core.domain.exception.MenuDomainException;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Getter
public class Product extends Menu {

    private final ProductID productID;

    private final Name name;

    private final Price price;

    private final Category category;

    private final Description description;

    private final Image image;

    private final List<IngredientID> ingredients;

    public Product(String name, Double price, Category category,
                   String description, String image, List<IngredientID> ingredients,
                   List<IngredientID> allIngredients) {
        this.productID = new ProductID();
        this.name = new Name(name);
        this.price = new Price(price);
        this.category = category;
        this.description = new Description(description);
        this.image = new Image(image);
        this.ingredients = ingredients;
        validateIngredients(ingredients, allIngredients);
    }

    private Product(ProductID productID, Name name, Price price, Category category,
                    Description description, Image image, List<IngredientID> ingredients,
                    Instant createdAt, Instant updatedAt) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
        this.creationTimestamp = createdAt;
        this.updateTimestamp = updatedAt;
    }

    public static Product copy(ProductID productID, Name name, Price price, Category category,
                               Description description, Image image, List<IngredientID> ingredients,
                               Instant createdAt, Instant updatedAt) {
        return new Product(productID, name, price, category, description, image, ingredients,
            createdAt, updatedAt);
    }

    public static Product deserialize(ObjectMapper deserializer, byte[] bytes) {
        try {
            return deserializer.readValue(bytes, Product.class);
        } catch (IOException e) {
            throw new ApplicationException("Error deserializing product", ApplicationError.APP_OO3);
        }
    }

    @Override
    public String getMenuId() {
        return productID.getId();
    }

    private void validateIngredients(List<IngredientID> inputIngredients, List<IngredientID> allIngredients) {
        if (!new HashSet<>(allIngredients).containsAll(inputIngredients)) {
            throw new MenuDomainException("Some of the ingredients are invalid.",
                MenuDomainError.MEN_001);
        }
    }

    public byte[] serialized(ObjectMapper serializer) {
        try {
            return serializer.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new ApplicationException("Error serializing product", ApplicationError.APP_OO2);
        }
    }
}
