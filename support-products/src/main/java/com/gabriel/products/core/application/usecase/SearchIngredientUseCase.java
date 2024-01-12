package com.gabriel.products.core.application.usecase;

import com.gabriel.products.core.application.query.SearchIngredientQuery;
import com.gabriel.products.core.domain.model.Category;
import com.gabriel.products.core.domain.model.Ingredient;
import com.gabriel.products.core.domain.port.IngredientRepository;
import com.gabriel.products.core.domain.port.ProductSearchParameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SearchIngredientUseCase {

    @Inject
    IngredientRepository ingredientRepository;

    public List<Ingredient> searchIngredient(SearchIngredientQuery query) {
        ProductSearchParameters parameters = new ProductSearchParameters(Category.valueOf(query.category().toUpperCase()));
        return ingredientRepository.searchBy(parameters);
    }
}
