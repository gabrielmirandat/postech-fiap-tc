package com.gabriel.products.core.domain.port;

import com.gabriel.products.core.domain.model.Product;

import java.util.List;

public interface ProductRepository {

    Product saveProduct(Product product);

    Product getById(String id);

    List<Product> searchBy(ProductSearchParameters parameters);

    void deleteProduct(String id);
}
