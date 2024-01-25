package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.port.IngredientRepository;
import com.gabriel.menu.core.domain.port.ProductSearchParameters;
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
