package com.gabriel.products.adapter.driven.persistence;

import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.ports.ProductRepository;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductPanacheMongoRepository implements ProductRepository {

    private PanacheContainerRepository template = new PanacheContainerRepository();

    @Override
    public Product saveProduct(Product product) {
        template.persist(product);
        return product;
    }

    @Override
    public Product getById(String id) {
        return template.find("productID", id).firstResult();
    }

    @Override
    public List<Product> searchBy(ProductSearchParameters parameters) {
        return template.list("category", parameters.category);
    }

    private class PanacheContainerRepository implements PanacheMongoRepository<Product> {
    }
}
