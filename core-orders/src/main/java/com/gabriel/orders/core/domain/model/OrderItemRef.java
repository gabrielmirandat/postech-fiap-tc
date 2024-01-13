package com.gabriel.orders.core.domain.model;

import com.gabriel.common.core.domain.base.ValueObject;
import com.gabriel.common.core.domain.model.id.IngredientID;
import com.gabriel.common.core.domain.model.id.ProductID;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemRef extends ValueObject {
    private final ProductID product;
    private final List<IngredientID> extras;

    public OrderItemRef(String product, List<String> extras) {
        this.product = new ProductID(product);
        this.extras = extras.stream().map(
            IngredientID::new).collect(Collectors.toList());
    }
}
