package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.queries.GetByProductIdQuery;
import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.ports.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RetrieveProductUseCase {

    @Inject
    ProductRepository productRepository;

    public Product getProductById(GetByProductIdQuery query) {
        return productRepository.getById(query.id());
    }
}
