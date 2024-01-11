package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import com.gabriel.orders.core.domain.valueobjects.ids.IngredientID;
import lombok.Getter;

@Getter
public class Extra extends ValueObject {

    private final IngredientID ingredientID;

    private final Name name;

    private final Price price;

    public Extra(IngredientID ingredientID, String name, Double value) {
        this.ingredientID = ingredientID;
        this.name = new Name(name);
        this.price = new Price(value);
    }
}
