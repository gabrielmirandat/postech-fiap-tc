package com.gabriel.orders.core.domain;

import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.domain.model.Product;

import java.time.Instant;

import static com.gabriel.orders.core.UtilsMock.generateRandomString;

public class ProductMock {

    public static Product validProduct(ProductId productId) {
        return new Product(productId, new Name(generateRandomString()),
            new Price(10.0), Instant.now());
    }
}
