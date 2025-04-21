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
        String productId = new ProductId().getId();
        String ingredientID = new IngredientId().getId();
        OrderItemRef orderItemRef = new OrderItemRef(productId, Collections.singletonList(ingredientID));

        assertThat(orderItemRef).isNotNull();
        assertThat(orderItemRef.getProductId().getId()).isEqualTo(productId);
        assertThat(orderItemRef.getExtrasIds().size()).isEqualTo(1);
        assertThat(orderItemRef.getExtrasIds().get(0).getId()).isEqualTo(ingredientID);
    }
}
