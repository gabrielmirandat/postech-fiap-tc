package com.gabriel.products.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.common.core.domain.base.AggregateRoot;
import com.gabriel.common.core.domain.model.Name;
import com.gabriel.common.core.domain.model.Price;
import com.gabriel.common.core.domain.model.id.IngredientID;
import com.gabriel.common.core.domain.model.id.ProductID;
import lombok.Getter;

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
}
