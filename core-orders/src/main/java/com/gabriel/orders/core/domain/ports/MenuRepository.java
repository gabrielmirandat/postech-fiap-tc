package com.gabriel.orders.core.domain.ports;

import com.gabriel.orders.core.domain.entities.Product;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;

public interface MenuRepository {

    void add(Product product);

    void update(Product product);

    void delete(ProductID productID);
}
