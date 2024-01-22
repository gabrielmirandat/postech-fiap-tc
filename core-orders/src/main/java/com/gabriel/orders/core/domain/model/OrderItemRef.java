package com.gabriel.orders.core.domain.model;

import com.gabriel.common.core.domain.base.ValueObject;
import com.gabriel.common.core.domain.model.id.IngredientID;
import com.gabriel.common.core.domain.model.id.ProductID;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderItemRef extends ValueObject {
    private final ProductID productId;
    private final List<IngredientID> extrasIds;

    public OrderItemRef(String productId, List<String> extrasIds) {
        this.productId = new ProductID(productId);
        this.extrasIds = extrasIds.stream().map(
            IngredientID::new).collect(Collectors.toList());
    }
}
