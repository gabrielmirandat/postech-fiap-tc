package com.gabriel.products.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.products.core.domain.base.AggregateRoot;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.valueobjects.Description;
import com.gabriel.products.core.domain.valueobjects.Name;
import com.gabriel.products.core.domain.valueobjects.Price;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
import com.gabriel.products.core.domain.valueobjects.ids.ProductID;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;

import java.util.List;

@Getter
@MongoEntity(collection = "products")
public class Product extends AggregateRoot {

    private final ProductID productID;

    private final Name name;

    private final Price price;

    private final Category category;

    private final Description description;

    private final List<IngredientID> ingredients;

    @JsonCreator
    public Product(ProductID productID, Name name, Price price, Category category,
                   Description description, List<IngredientID> ingredients) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.ingredients = ingredients;
    }

    public Product(ProductID productID, String name, Double price, Category category,
                   String description, List<IngredientID> ingredients) {
        this.productID = productID;
        this.name = new Name(name);
        this.price = new Price(price);
        this.category = category;
        this.description = new Description(description);
        this.ingredients = ingredients;
    }

    public Product(String name, Double price, Category category,
                   String description, List<IngredientID> ingredients) {
        this.productID = new ProductID();
        this.name = new Name(name);
        this.price = new Price(price);
        this.category = category;
        this.description = new Description(description);
        this.ingredients = ingredients;
    }
}
