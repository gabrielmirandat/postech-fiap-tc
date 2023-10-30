package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.Name;
import com.gabriel.orders.core.domain.valueobjects.Price;
import com.gabriel.orders.core.domain.valueobjects.ids.IngredientID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Extra extends Entity {

    private final IngredientID ingredientID;

    private final Name name;

    private final Price price;

    public Extra(IngredientID ingredientID, String name, Double value) {
        this.ingredientID = ingredientID;
        this.name = new Name(name);
        this.price = new Price(value);
    }
}
