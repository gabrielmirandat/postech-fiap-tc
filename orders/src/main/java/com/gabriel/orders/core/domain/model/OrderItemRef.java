package com.gabriel.orders.core.domain.model;

import com.gabriel.core.domain.ValueObject;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemRef extends ValueObject {
    private final ProductID productId;
    private final List<IngredientID> extrasIds;

    public OrderItemRef(String productId, List<String> extrasIds) {
        this.productId = new ProductID(productId);
        this.extrasIds = extrasIds.stream().map(
            IngredientID::new).collect(Collectors.toList());
    }

    public ProductID getProductId() {
        return productId;
    }

    public List<IngredientID> getExtrasIds() {
        return extrasIds;
    }
}
