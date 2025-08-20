package com.gabriel.orders.core.domain;

import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.domain.model.Product;

import java.time.Instant;

import static com.gabriel.orders.core.UtilsMock.generateRandomString;

public class ProductMock {

    public static Product validProduct(ProductId productId) {
        return Product.create(productId, Name.newBuilder().setValue(generateRandomString()).build(),
            Price.newBuilder().setValue(10.0).build(), Instant.now());
    }
}
