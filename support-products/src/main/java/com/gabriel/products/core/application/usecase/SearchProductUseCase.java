package com.gabriel.products.core.application.usecase;

import com.gabriel.products.core.application.query.SearchProductQuery;
import com.gabriel.products.core.domain.model.Category;
import com.gabriel.products.core.domain.model.Product;
import com.gabriel.products.core.domain.port.ProductRepository;
import com.gabriel.products.core.domain.port.ProductSearchParameters;
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
