package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.queries.SearchProductQuery;
import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.ports.ProductRepository;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SearchProductUseCase {

    @Inject
    ProductRepository productRepository;

    public List<Product> searchProduct(SearchProductQuery query) {
        ProductSearchParameters parameters = new ProductSearchParameters(Category.valueOf(query.category().toUpperCase()));
        return productRepository.searchBy(parameters);
    }
}
