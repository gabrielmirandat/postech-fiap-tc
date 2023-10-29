package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.Price;
import com.gabriel.orders.core.domain.valueobjects.ids.IngredientID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Extra extends Entity {

    private final IngredientID ingredientID;

    private final Price price;
}
