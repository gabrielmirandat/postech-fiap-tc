package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.commands.DeleteProductCommand;
import com.gabriel.products.core.domain.ports.ProductRepository;
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
