package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.ApplicationError;
import com.gabriel.model.ApplicationException;
import com.gabriel.model.Description;
import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.menu.core.domain.exception.MenuDomainError;
import com.gabriel.menu.core.domain.exception.MenuDomainException;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

public class Product extends Menu {

    private final ProductId productId;

    private final Name name;

    private final Price price;

    private final Category category;

    private final Description description;

    private final Image image;

    private final List<IngredientId> ingredients;

    public Product(String name, Double price, Category category,
                   String description, String image, List<IngredientId> ingredients,
                   List<IngredientId> allIngredients) {
        this.productId = new ProductId();
        this.name = new Name(name);
        this.price = new Price(price);
        this.category = category;
        this.description = new Description(description);
        this.image = new Image(image);
        this.ingredients = ingredients;
        validateIngredients(ingredients, allIngredients);
    }

    /**
     * Constructor for Jackson deserialization.
     */
    @JsonCreator
    Product(@JsonProperty("menuId") ProductId productId, @JsonProperty("name") Name name,
            @JsonProperty("price") Price price, @JsonProperty("category") Category category,
            @JsonProperty("description") Description description, @JsonProperty("image") Image image,
            @JsonProperty("ingredients") List<IngredientId> ingredients,
            @JsonProperty("createdAt") Instant createdAt, @JsonProperty("updatedAt") Instant updatedAt) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
        this.creationTimestamp = createdAt;
        this.updateTimestamp = updatedAt;
    }

    public static Product copy(ProductId productId, Name name, Price price, Category category,
                               Description description, Image image, List<IngredientId> ingredients,
                               Instant createdAt, Instant updatedAt) {
        return new Product(productId, name, price, category, description, image, ingredients,
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
        return productId.getId();
    }

    private void validateIngredients(List<IngredientId> inputIngredients, List<IngredientId> allIngredients) {
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

    public ProductId getProductId() {
        return productId;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public Description getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public List<IngredientId> getIngredients() {
        return ingredients;
    }
}
