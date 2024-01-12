package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controller.ProductsApi;
import com.gabriel.products.adapter.driver.api.mapper.ProductMapper;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.adapter.driver.api.models.ProductCreated;
import com.gabriel.products.adapter.driver.api.models.ProductRequest;
import com.gabriel.products.adapter.driver.api.models.ProductResponse;
import com.gabriel.products.core.application.command.CreateProductCommand;
import com.gabriel.products.core.application.command.DeleteProductCommand;
import com.gabriel.products.core.application.query.GetByProductIdQuery;
import com.gabriel.products.core.application.query.SearchProductQuery;
import com.gabriel.products.core.application.usecase.CreateProductUseCase;
import com.gabriel.products.core.application.usecase.DeleteProductUseCase;
import com.gabriel.products.core.application.usecase.RetrieveProductUseCase;
import com.gabriel.products.core.application.usecase.SearchProductUseCase;
import com.gabriel.products.core.domain.model.Product;
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
        Product product = retrieveProductUseCase.getProductById(query);
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> findProductsByQuery(ProductCategoryDTO category) {
        SearchProductQuery query = new SearchProductQuery(category.toString());
        List<Product> products = searchProductUseCase.searchProduct(query);
        return productMapper.toResponse(products);
    }
}
