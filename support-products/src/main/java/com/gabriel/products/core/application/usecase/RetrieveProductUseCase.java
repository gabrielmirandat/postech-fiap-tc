package com.gabriel.products.core.application.usecase;

import com.gabriel.common.core.domain.model.id.IngredientID;
import com.gabriel.products.adapter.driver.api.mapper.ProductMapper;
import com.gabriel.products.adapter.driver.api.models.ProductResponse;
import com.gabriel.products.core.application.query.GetByIngredientIdsQuery;
import com.gabriel.products.core.application.query.GetByProductIdQuery;
import com.gabriel.products.core.application.query.GetByProductQuery;
import com.gabriel.products.core.domain.model.Ingredient;
import com.gabriel.products.core.domain.model.Product;
import com.gabriel.products.core.domain.port.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class RetrieveProductUseCase {

    @Inject
    ProductRepository productRepository;

    @Inject
    RetrieveIngredientUseCase retrieveIngredientUseCase;

    @Inject
    ProductMapper productMapper;


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
            retrieveIngredientUseCase.getIngredientsById(
                new GetByIngredientIdsQuery(
                    product.getIngredients().stream()
                        .map(IngredientID::getId).toList()));
        return productMapper.toResponse(product, ingredientList);
    }
}
