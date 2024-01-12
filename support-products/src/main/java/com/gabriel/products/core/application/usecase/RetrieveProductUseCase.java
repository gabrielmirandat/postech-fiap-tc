package com.gabriel.products.core.application.usecase;

import com.gabriel.products.core.application.query.GetByProductIdQuery;
import com.gabriel.products.core.domain.model.Product;
import com.gabriel.products.core.domain.port.ProductRepository;
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
