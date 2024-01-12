package com.gabriel.products.core.application.usecase;

import com.gabriel.products.core.application.command.DeleteProductCommand;
import com.gabriel.products.core.domain.port.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteProductUseCase {

    @Inject
    ProductRepository productRepository;

    // it may not work with quarkus
    public void deleteProduct(DeleteProductCommand command) {
        productRepository.deleteProduct(command.productId());
    }
}
