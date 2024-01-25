package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.command.CreateProductCommand;
import com.gabriel.menu.core.domain.event.ProductCreatedEvent;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.menu.core.domain.port.ProductPublisher;
import com.gabriel.menu.core.domain.port.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateProductUseCase {

    @Inject
    ProductRepository productRepository;

    @Inject
    VerifyIngredientMenuUseCase verifyIngredientMenuUseCase;

    @Inject
    ProductPublisher productPublisher;

    // it may not work with quarkus
    public Product createProduct(CreateProductCommand command) {
        try {
            verifyIngredientMenuUseCase.validate(command.category(),
                command.ingredients());
            Product product = new Product(command.name(), command.price(), command.category(), command.description(), command.image(), command.ingredients());
            productRepository.saveProduct(product);
            productPublisher.productCreated(new ProductCreatedEvent(product));
            return product;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
