package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.application.query.SearchMenuQuery;
import com.gabriel.menu.core.application.query.SearchProductQuery;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Menu;
import com.gabriel.menu.core.domain.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class MenuUseCase {

    @Inject
    IngredientUseCase ingredientUseCase;

    @Inject
    IngredientMenuUseCase ingredientMenuUseCase;

    @Inject
    ProductUseCase productUseCase;

    @Inject
    ProductMenuUseCase productMenuUseCase;

    public List<Menu> searchMenuByCategory(SearchMenuQuery searchMenuQuery) {
        List<Ingredient> ingredients = ingredientUseCase.searchIngredient(
            new SearchIngredientQuery(searchMenuQuery.category()));
        List<Product> products = productUseCase.searchProduct(
            new SearchProductQuery(searchMenuQuery.category()));
        return Stream.concat(ingredients.stream(), products.stream()).toList();
    }

    public List<Menu> searchMenu() {
        List<Ingredient> ingredients = ingredientMenuUseCase.retrieveMenu();
        List<Product> products = productMenuUseCase.retrieveMenu();
        return Stream.concat(ingredients.stream(), products.stream()).toList();
    }
}
