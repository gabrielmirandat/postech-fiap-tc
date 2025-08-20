package com.gabriel.orders.core.domain.model;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.domain.model.OrderItemRef;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemRefTest {

    @Test
    public void testOrderItemRef() {
        String productId = ProductId.newBuilder().setValue("product-123").build().getValue();
        String ingredientID = IngredientId.newBuilder().setValue("ingredient-123").build().getValue();
        OrderItemRef orderItemRef = new OrderItemRef(productId, Collections.singletonList(ingredientID));

        assertThat(orderItemRef).isNotNull();
        assertThat(orderItemRef.getProductId().getValue()).isEqualTo(productId);
        assertThat(orderItemRef.getExtrasIds().size()).isEqualTo(1);
        assertThat(orderItemRef.getExtrasIds().get(0).getValue()).isEqualTo(ingredientID);
    }
}
