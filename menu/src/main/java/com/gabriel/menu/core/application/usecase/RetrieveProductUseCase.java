package com.gabriel.menu.core.application.usecase;

import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.menu.adapter.driver.api.mapper.ProductMapper;
import com.gabriel.menu.core.application.query.GetByIngredientIdsQuery;
import com.gabriel.menu.core.application.query.GetByProductIdQuery;
import com.gabriel.menu.core.application.query.GetByProductQuery;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.menu.core.domain.port.ProductRepository;
import com.gabriel.specs.menu.models.ProductResponse;
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
