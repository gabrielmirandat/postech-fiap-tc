package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.valueobjects.ids.IngredientID;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemRef {
    private final ProductID product;
    private final List<IngredientID> extras;

    public OrderItemRef(String product, List<String> extras) {
        this.product = new ProductID(product);
        this.extras = extras.stream().map(
                IngredientID::new).collect(Collectors.toList());
    }
}
