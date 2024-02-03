package com.gabriel.menu.core.application.usecase;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.menu.adapter.driver.api.mapper.ProductMapper;
import com.gabriel.menu.core.application.command.CreateProductCommand;
import com.gabriel.menu.core.application.command.DeleteProductCommand;
import com.gabriel.menu.core.application.query.GetByIngredientIdsQuery;
import com.gabriel.menu.core.application.query.GetByProductIdQuery;
import com.gabriel.menu.core.application.query.GetByProductQuery;
import com.gabriel.menu.core.application.query.SearchProductQuery;
import com.gabriel.menu.core.domain.event.ProductCreatedEvent;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.menu.core.domain.port.ProductPublisher;
import com.gabriel.menu.core.domain.port.ProductRepository;
import com.gabriel.menu.core.domain.port.ProductSearchParameters;
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
    IngredientMenuUseCase ingredientMenuUseCase;

    @Inject
    ProductPublisher productPublisher;

    @Inject
    ProductMapper productMapper;

    public Product createProduct(CreateProductCommand command) {
        ingredientMenuUseCase.matchToMenuByCategory(command.category(),
            command.ingredients());
        Product product = new Product(command.name(), command.price(), command.category(), command.description(), command.image(), command.ingredients());
        productRepository.saveProduct(product);
        productPublisher.productCreated(new ProductCreatedEvent(product));
        return product;
    }

    public void deleteProduct(DeleteProductCommand command) {
        productRepository.deleteProduct(command.productId());
    }

    public Product getProductById(GetByProductIdQuery query) {
        return productRepository.getById(query.id());
    }

    public ProductResponse getResponseById(GetByProductIdQuery query) {
        Product product = getProductById(query);
        return getResponseByProduct(new GetByProductQuery(product));
    }

    public ProductResponse getResponseByProduct(GetByProductQuery query) {
        Product product = query.product();
        List<Ingredient> ingredientList =
            ingredientUseCase.getIngredientsById(
                new GetByIngredientIdsQuery(
                    product.getIngredients().stream()
                        .map(IngredientID::getId).toList()));
        return productMapper.toResponse(product, ingredientList);
    }

    public List<Product> searchProduct(SearchProductQuery query) {
        ProductSearchParameters parameters = new ProductSearchParameters(Category.valueOf(query.category().toUpperCase()));
        return productRepository.searchBy(parameters);
    }

    public List<ProductResponse> searchProductResponse(SearchProductQuery query) {
        List<Product> products = searchProduct(query);
        return products.stream()
            .map(product -> getResponseByProduct(
                new GetByProductQuery(product))).toList();
    }
}
