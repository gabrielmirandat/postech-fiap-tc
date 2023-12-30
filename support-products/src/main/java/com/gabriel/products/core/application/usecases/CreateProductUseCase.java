package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.commands.CreateProductCommand;
import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.ports.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateProductUseCase {

    @Inject
    ProductRepository productRepository;

    // it may not work with quarkus
    public Product createProduct(CreateProductCommand command) {
        Product product = new Product(command.name(), command.price(), command.category(), command.description(), command.image(), command.ingredients());
        productRepository.saveProduct(product);
        return product;
    }
}
