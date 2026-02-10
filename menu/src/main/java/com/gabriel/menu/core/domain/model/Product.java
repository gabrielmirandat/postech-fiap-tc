package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        this.productId = ProductId.newBuilder().build();
        this.name = Name.newBuilder().setValue(name).build();
        this.price = Price.newBuilder().setValue(price).build();
        this.category = category;
        this.description = Description.newBuilder().setValue(description).build();
        this.image = new Image(image);
        this.ingredients = ingredients;
        validateIngredients(ingredients, allIngredients);
    }

    /**
     * Constructor for Jackson deserialization.
     */
    @JsonCreator
    Product(@JsonProperty("menuId") String menuId, @JsonProperty("name") String nameStr,
            @JsonProperty("price") Double priceValue, @JsonProperty("category") Category category,
            @JsonProperty("description") String descriptionStr, @JsonProperty("image") String imageStr,
            @JsonProperty("ingredients") List<String> ingredientIds,
            @JsonProperty("createdAt") Instant createdAt, @JsonProperty("updatedAt") Instant updatedAt) {
        this.productId = ProductId.newBuilder().setValue(menuId).build();
        this.name = Name.newBuilder().setValue(nameStr).build();
        this.price = Price.newBuilder().setValue(priceValue).build();
        this.category = category;
        this.description = Description.newBuilder().setValue(descriptionStr).build();
        this.image = new Image(imageStr);
        this.ingredients = ingredientIds != null ? ingredientIds.stream()
            .map(id -> IngredientId.newBuilder().setValue(id).build())
            .toList() : List.of();
        this.creationTimestamp = createdAt;
        this.updateTimestamp = updatedAt;
    }

    public static Product copy(ProductId productId, Name name, Price price, Category category,
                               Description description, Image image, List<IngredientId> ingredients,
                               Instant createdAt, Instant updatedAt) {
        List<String> ingredientIds = ingredients != null ? ingredients.stream()
            .map(IngredientId::getValue)
            .toList() : List.of();
        return new Product(productId.getValue(), name.getValue(), price.getValue(), category, description.getValue(), image.getUrl(), ingredientIds,
            createdAt, updatedAt);
    }

    public static Product deserialize(ObjectMapper deserializer, byte[] bytes) {
        try {
            return deserializer.readValue(bytes, Product.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing product: " + e.getMessage());
        }
    }

    @Override
    public String getMenuId() {
        return productId.getValue();
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
            throw new RuntimeException("Error serializing product: " + e.getMessage());
        }
    }

    @JsonIgnore
    public ProductId getProductId() {
        return productId;
    }
    
    @JsonProperty("menuId")
    public String getMenuIdString() {
        return productId.getValue();
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

    @JsonIgnore
    public Description getDescription() {
        return description;
    }
    
    @JsonProperty("description")
    public String getDescriptionString() {
        return description.getValue();
    }

    @JsonIgnore
    public Image getImage() {
        return image;
    }
    
    @JsonProperty("image")
    public String getImageString() {
        return image.getUrl();
    }

    @JsonIgnore
    public List<IngredientId> getIngredients() {
        return ingredients;
    }
    
    @JsonProperty("ingredients")
    public List<String> getIngredientsAsStrings() {
        return ingredients.stream().map(IngredientId::getValue).toList();
    }
}
