package com.gabriel.products.core.domain.ports;

import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;

import java.util.List;

public interface ProductRepository {

    Product saveProduct(Product product);

    Product getById(String id);

    List<Product> searchBy(ProductSearchParameters parameters);
}
