package com.gabriel.products.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.products.core.domain.base.AggregateRoot;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.valueobjects.Description;
import com.gabriel.products.core.domain.valueobjects.Name;
import com.gabriel.products.core.domain.valueobjects.Price;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
import com.gabriel.products.core.domain.valueobjects.ids.ProductID;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Product extends AggregateRoot {

    private final ProductID productID;

    private final Name name;

    private final Price price;

    private final Category category = null;

    private final Description description = null;

    private final List<IngredientID> ingredients = new ArrayList<>();

    @JsonCreator
    public Product(ProductID productID, Name name, Price value) {
        this.productID = productID;
        this.name = name;
        this.price = value;
    }

    public Product(ProductID productID, String name, Double value) {
        this.productID = productID;
        this.name = new Name(name);
        this.price = new Price(value);
    }
}
