package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.*;
import com.gabriel.orders.core.domain.valueobjects.ids.IngredientID;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Product extends Entity {

    private final ProductID productID;

    private final Price price;

    public Product(ProductID productID, Double value) {
        this.productID = productID;
        this.price = new Price(value);
    }
}
