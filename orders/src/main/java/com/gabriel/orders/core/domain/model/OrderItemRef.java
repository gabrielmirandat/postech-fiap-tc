package com.gabriel.orders.core.domain.model;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemRef {
    private final ProductId productId;
    private final List<IngredientId> extrasIds;

    public OrderItemRef(String productId, List<String> extrasIds) {
        this.productId = new ProductId(productId);
        this.extrasIds = extrasIds.stream().map(
            IngredientId::new).collect(Collectors.toList());
    }

    public ProductId getProductId() {
        return productId;
    }

    public List<IngredientId> getExtrasIds() {
        return extrasIds;
    }
}
