package com.gabriel.products.core.domain.model;

import com.gabriel.common.core.domain.base.AggregateRoot;
import com.gabriel.common.core.domain.model.Name;
import com.gabriel.common.core.domain.model.Price;
import com.gabriel.common.core.domain.model.id.IngredientID;
import lombok.Getter;

@Getter
public class Ingredient extends AggregateRoot {

    private final IngredientID ingredientID;

    private final Name name;

    private final Price price;

    private final Category category;

    private final Weight weight;

    private final boolean isExtra;

    public Ingredient(IngredientID ingredientID, Name name,
                      Category category, Price price, Weight weight, boolean isExtra) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.weight = weight;
        this.isExtra = isExtra;
    }

    public Ingredient(IngredientID ingredientID, String name,
                      Category category, Double price, Double weight, boolean isExtra) {
        this.ingredientID = ingredientID;
        this.name = new Name(name);
        this.category = category;
        this.price = new Price(price);
        this.weight = new Weight(weight);
        this.isExtra = isExtra;
    }

    public Ingredient(String name, Category category, Double price,
                      Double weight, boolean isExtra) {
        this.ingredientID = new IngredientID();
        this.name = new Name(name);
        this.category = category;
        this.price = new Price(price);
        this.weight = new Weight(weight);
        this.isExtra = isExtra;
    }
}
