package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.query.SearchProductQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductMenuUseCase {

    @Inject
    ProductUseCase productUseCase;

    public List<Product> retrieveMenu() {
        List<Product> products = new ArrayList<>();

        for (Category category : Category.values()) {
            products.addAll(
                productUseCase.searchProduct(
                    new SearchProductQuery(category)));
        }
        return products;
    }
}
