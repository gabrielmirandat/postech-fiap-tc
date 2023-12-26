package com.gabriel.products.adapter.driven.persistence;

import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.ports.ProductRepository;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;

import java.util.List;

public class ProductJpaPostgreSQLRepository implements ProductRepository {
    @Override
    public Product saveProduct(Product product) {
        return null;
    }

    @Override
    public Product getById(String id) {
        return null;
    }

    @Override
    public List<Product> searchBy(ProductSearchParameters parameters) {
        return null;
    }

    @Override
    public void deleteProduct(String id) {
    }
}
