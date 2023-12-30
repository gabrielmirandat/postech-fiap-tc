package com.gabriel.products.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.products.core.domain.base.AggregateRoot;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.valueobjects.Description;
import com.gabriel.products.core.domain.valueobjects.Image;
import com.gabriel.products.core.domain.valueobjects.Name;
import com.gabriel.products.core.domain.valueobjects.Price;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
import com.gabriel.products.core.domain.valueobjects.ids.ProductID;
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
