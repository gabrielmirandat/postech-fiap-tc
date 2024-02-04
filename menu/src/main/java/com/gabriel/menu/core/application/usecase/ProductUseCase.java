package com.gabriel.menu.core.application.usecase;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.menu.adapter.driver.api.mapper.MenuMapper;
import com.gabriel.menu.core.application.command.CreateProductCommand;
import com.gabriel.menu.core.application.command.DeleteProductCommand;
import com.gabriel.menu.core.application.query.*;
import com.gabriel.menu.core.domain.event.ProductCreatedEvent;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.menu.core.domain.port.ProductPublisher;
import com.gabriel.menu.core.domain.port.ProductRepository;
import com.gabriel.menu.core.domain.port.SearchParameters;
import com.gabriel.specs.menu.models.ProductResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProductUseCase {

    @Inject
    ProductRepository productRepository;

    @Inject
    IngredientUseCase ingredientUseCase;

    @Inject
    ProductPublisher productPublisher;

    private List<IngredientID> idsOf(List<Ingredient> ingredients) {
        return ingredients.stream().map(Ingredient::getIngredientID).toList();
    }

    public Product createProduct(CreateProductCommand command) {
        List<IngredientID> categoryMenu =
            idsOf(ingredientUseCase.searchIngredient(
                new SearchIngredientQuery(command.category())));
        Product product = new Product(command.name(), command.price(), command.category(),
            command.description(), command.image(), command.ingredients(), categoryMenu);
        productRepository.saveProduct(product);
        productPublisher.productCreated(new ProductCreatedEvent(product));
        return product;
    }

    public void deleteProduct(DeleteProductCommand command) {

        productRepository.deleteProduct(command.deleteId());
    }

    public Product getProductById(GetByProductIdQuery query) {
        return productRepository.getById(query.searchId());
    }

    public ProductResponse getResponseById(GetByProductIdQuery query) {
        Product product = getProductById(query);
        return getResponseByProduct(new GetByProductQuery(product));
    }

    public ProductResponse getResponseByProduct(GetByProductQuery query) {
        Product product = query.product();
        List<Ingredient> ingredientList = ingredientUseCase.getIngredientsById(
            new GetByIngredientIdsQuery(product.getIngredients()));
        return MenuMapper.toResponse(product, ingredientList);
    }

    public List<Product> searchProduct(SearchProductQuery query) {
        SearchParameters parameters = new SearchParameters(query.category());
        return productRepository.searchBy(parameters);
    }

    public List<ProductResponse> searchProductResponse(SearchProductQuery query) {
        List<Product> products = searchProduct(query);
        return products.stream()
            .map(product -> getResponseByProduct(
                new GetByProductQuery(product))).toList();
    }
}
