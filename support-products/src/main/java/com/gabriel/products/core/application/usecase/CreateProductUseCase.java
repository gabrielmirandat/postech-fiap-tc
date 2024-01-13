package com.gabriel.products.core.application.usecase;

import com.gabriel.products.core.application.command.CreateProductCommand;
import com.gabriel.products.core.domain.event.ProductCreatedEvent;
import com.gabriel.products.core.domain.model.Product;
import com.gabriel.products.core.domain.port.ProductPublisher;
import com.gabriel.products.core.domain.port.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateProductUseCase {

    @Inject
    ProductRepository productRepository;

    @Inject
    VerifyIngredientUseCase verifyIngredientUseCase;

    @Inject
    ProductPublisher productPublisher;

    // it may not work with quarkus
    public Product createProduct(CreateProductCommand command) {
        try {
            verifyIngredientUseCase.verifyIngredientsConsistency(command.ingredients());
            Product product = new Product(command.name(), command.price(), command.category(), command.description(), command.image(), command.ingredients());
            productRepository.saveProduct(product);
            productPublisher.productCreated(new ProductCreatedEvent(product));
            return product;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
