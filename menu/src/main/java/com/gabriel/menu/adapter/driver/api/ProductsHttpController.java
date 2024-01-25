package com.gabriel.menu.adapter.driver.api;

import com.gabriel.menu.adapter.driver.api.controller.ProductsApi;
import com.gabriel.menu.adapter.driver.api.mapper.ProductMapper;
import com.gabriel.menu.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.menu.adapter.driver.api.models.ProductCreated;
import com.gabriel.menu.adapter.driver.api.models.ProductRequest;
import com.gabriel.menu.adapter.driver.api.models.ProductResponse;
import com.gabriel.menu.core.application.command.CreateProductCommand;
import com.gabriel.menu.core.application.command.DeleteProductCommand;
import com.gabriel.menu.core.application.query.GetByProductIdQuery;
import com.gabriel.menu.core.application.query.SearchProductQuery;
import com.gabriel.menu.core.application.usecase.CreateProductUseCase;
import com.gabriel.menu.core.application.usecase.DeleteProductUseCase;
import com.gabriel.menu.core.application.usecase.RetrieveProductUseCase;
import com.gabriel.menu.core.application.usecase.SearchProductUseCase;
import com.gabriel.menu.core.domain.model.Product;
import jakarta.inject.Inject;

import java.util.List;

public class ProductsHttpController implements ProductsApi {

    @Inject
    CreateProductUseCase createProductUseCase;

    @Inject
    DeleteProductUseCase deleteProductUseCase;

    @Inject
    RetrieveProductUseCase retrieveProductUseCase;

    @Inject
    SearchProductUseCase searchProductUseCase;

    @Inject
    ProductMapper productMapper;

    @Override
    public ProductCreated addProduct(ProductRequest productRequest) {
        CreateProductCommand command = productMapper.toCommand(productRequest);
        Product newProduct = createProductUseCase.createProduct(command);
        return new ProductCreated().productId(newProduct.getProductID().getId());
    }

    @Override
    public void deleteProduct(String productId) {
        DeleteProductCommand command = new DeleteProductCommand(productId);
        deleteProductUseCase.deleteProduct(command);
    }

    @Override
    public ProductResponse getProductById(String productId) {
        GetByProductIdQuery query = new GetByProductIdQuery(productId);
        return retrieveProductUseCase.getResponseById(query);
    }

    @Override
    public List<ProductResponse> findProductsByQuery(ProductCategoryDTO category) {
        SearchProductQuery query = new SearchProductQuery(category.toString());
        return searchProductUseCase.searchProductResponse(query);
    }
}
