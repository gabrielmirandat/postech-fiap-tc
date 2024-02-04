package com.gabriel.menu.core.domain.port;

import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.menu.core.domain.model.Product;

import java.util.List;

public interface ProductRepository {

    Product saveProduct(Product product);

    Product getById(ProductID id);

    List<Product> searchBy(SearchParameters parameters);

    void deleteProduct(ProductID id);
}
