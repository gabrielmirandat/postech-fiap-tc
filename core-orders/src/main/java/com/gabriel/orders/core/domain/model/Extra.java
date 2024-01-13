package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.common.core.domain.base.ValueObject;
import com.gabriel.common.core.domain.model.Name;
import com.gabriel.common.core.domain.model.Price;
import com.gabriel.common.core.domain.model.id.IngredientID;
import lombok.Getter;

@Getter
public class Extra extends ValueObject {

    private final IngredientID ingredientID;

    private final Name name;

    private final Price price;

    @JsonCreator
    public Extra(IngredientID ingredientID,
                 Name name,
                 Price value) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.price = value;
    }

    public Extra(IngredientID ingredientID, String name, Double value) {
        this.ingredientID = ingredientID;
        this.name = new Name(name);
        this.price = new Price(value);
    }
}
