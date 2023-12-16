package com.gabriel.products.core.domain.entities;

import com.gabriel.products.core.domain.base.AggregateRoot;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.valueobjects.Name;
import com.gabriel.products.core.domain.valueobjects.Price;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
import lombok.Getter;

@Getter
public class Ingredient extends AggregateRoot {

    private final IngredientID ingredientID;

    private final Name name;

    private final Price price;

    private final Category category;

    public Ingredient(IngredientID ingredientID, String name, Double value, Category category) {
        this.ingredientID = ingredientID;
        this.name = new Name(name);
        this.price = new Price(value);
        this.category = category;
    }
}
