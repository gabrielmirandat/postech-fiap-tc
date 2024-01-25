package com.gabriel.menu.core.domain.port;

import com.gabriel.menu.core.domain.model.Product;

import java.util.List;

public interface ProductRepository {

    Product saveProduct(Product product);

    Product getById(String id);

    List<Product> searchBy(ProductSearchParameters parameters);

    void deleteProduct(String id);
}
