package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.queries.SearchIngredientQuery;
import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.ports.IngredientRepository;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;
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
