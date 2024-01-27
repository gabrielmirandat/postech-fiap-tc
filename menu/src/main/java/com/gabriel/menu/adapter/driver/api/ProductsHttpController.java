package com.gabriel.menu.adapter.driver.api;

import com.gabriel.menu.adapter.driver.api.mapper.ProductMapper;
import com.gabriel.menu.core.application.command.CreateProductCommand;
import com.gabriel.menu.core.application.command.DeleteProductCommand;
import com.gabriel.menu.core.application.query.GetByProductIdQuery;
import com.gabriel.menu.core.application.query.SearchProductQuery;
import com.gabriel.menu.core.application.usecase.ProductUseCase;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.specs.menu.ProductsApi;
import com.gabriel.specs.menu.models.ProductCategoryDTO;
import com.gabriel.specs.menu.models.ProductCreated;
import com.gabriel.specs.menu.models.ProductRequest;
import com.gabriel.specs.menu.models.ProductResponse;
import jakarta.inject.Inject;

import java.util.List;

public class ProductsHttpController implements ProductsApi {

    @Inject
    ProductUseCase productUseCase;

    @Inject
    ProductMapper productMapper;

    @Override
    public ProductCreated addProduct(ProductRequest productRequest) {
        CreateProductCommand command = productMapper.toCommand(productRequest);
        Product newProduct = productUseCase.createProduct(command);
        return new ProductCreated().productId(newProduct.getProductID().getId());
    }

    @Override
    public void deleteProduct(String productId) {
        DeleteProductCommand command = new DeleteProductCommand(productId);
        productUseCase.deleteProduct(command);
    }

    @Override
    public ProductResponse getProductById(String productId) {
        GetByProductIdQuery query = new GetByProductIdQuery(productId);
        return productUseCase.getResponseById(query);
    }

    @Override
    public List<ProductResponse> findProductsByQuery(ProductCategoryDTO category) {
        SearchProductQuery query = new SearchProductQuery(category.toString());
        return productUseCase.searchProductResponse(query);
    }
}
