package com.gabriel.menu.core.domain.port;

import com.gabriel.model.ProductId;
import com.gabriel.menu.core.domain.model.Product;

import java.util.List;

public interface ProductRepository {

    Product saveProduct(Product product);

    Product getById(ProductId id);

    List<Product> searchBy(SearchParameters parameters);

    void deleteProduct(ProductId id);
}
