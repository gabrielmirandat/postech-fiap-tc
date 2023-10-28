package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.Price;
import com.gabriel.orders.core.domain.valueobjects.ProductID;

public class Ingredient extends Entity {

    private final ProductID ingredientID;

    private final Price price;
}
