package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.query.GetByProductQuery;
import com.gabriel.menu.core.application.query.SearchProductQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.menu.core.domain.port.ProductRepository;
import com.gabriel.menu.core.domain.port.ProductSearchParameters;
import com.gabriel.specs.menu.models.ProductResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SearchProductUseCase {

    @Inject
    ProductRepository productRepository;

    @Inject
    RetrieveProductUseCase retrieveProductUseCase;

    public List<Product> searchProduct(SearchProductQuery query) {
        ProductSearchParameters parameters = new ProductSearchParameters(Category.valueOf(query.category().toUpperCase()));
        return productRepository.searchBy(parameters);
    }

    public List<ProductResponse> searchProductResponse(SearchProductQuery query) {
        List<Product> products = searchProduct(query);
        return products.stream()
            .map(product -> retrieveProductUseCase.getResponseByProduct(
                new GetByProductQuery(product))).toList();
    }
}
