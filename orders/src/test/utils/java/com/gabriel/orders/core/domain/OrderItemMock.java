package com.gabriel.orders.core.domain;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.OrderItem;
import com.gabriel.orders.core.domain.model.Product;

import java.util.Collections;

import static com.gabriel.orders.core.domain.ExtraMock.validExtra;
import static com.gabriel.orders.core.domain.ProductMock.validProduct;

public class OrderItemMock {

    public static OrderItem validOrderItem(boolean withExtra) {
        Product product = validProduct(new ProductId());

        if (withExtra) {
            Extra extra = validExtra(new IngredientId());
            return new OrderItem(product, Collections.singletonList(extra));
        }

        return new OrderItem(product);
    }
}
