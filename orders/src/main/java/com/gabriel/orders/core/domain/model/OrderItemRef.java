package com.gabriel.orders.core.domain.model;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemRef {
    private final ProductId productId;
    private final List<IngredientId> extrasIds;

    public OrderItemRef(String productId, List<String> extrasIds) {
        this.productId = ProductId.newBuilder().setValue(productId).build();
        this.extrasIds = extrasIds.stream().map(
            id -> IngredientId.newBuilder().setValue(id).build()).collect(Collectors.toList());
    }

    public ProductId getProductId() {
        return productId;
    }

    public List<IngredientId> getExtrasIds() {
        return extrasIds;
    }
}
