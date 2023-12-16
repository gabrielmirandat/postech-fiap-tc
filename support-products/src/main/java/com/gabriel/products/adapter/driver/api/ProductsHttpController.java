package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controller.ProductsApi;
import com.gabriel.products.adapter.driver.api.mappers.ProductMapper;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.adapter.driver.api.models.ProductCreated;
import com.gabriel.products.adapter.driver.api.models.ProductRequest;
import com.gabriel.products.adapter.driver.api.models.ProductResponse;
import com.gabriel.products.core.application.commands.CreateProductCommand;
import com.gabriel.products.core.application.usecases.CreateProductUseCase;
import com.gabriel.products.core.domain.entities.Product;
import jakarta.inject.Inject;

public class ProductsHttpController implements ProductsApi {

    @Inject
    private CreateProductUseCase createProductUseCase;

    @Inject
    private ProductMapper productMapper;

    @Override
    public ProductCreated addProduct(ProductRequest productRequest) {
        CreateProductCommand command = productMapper.toCommand(productRequest);
        Product newProduct = createProductUseCase.createProduct(command);
        return new ProductCreated().productId(newProduct.getProductID().toString());
    }

    @Override
    public void deleteProduct(String productId) {
    }

    @Override
    public ProductResponse findProductsByQuery(ProductCategoryDTO category) {
        return null;
    }

    @Override
    public ProductResponse getProductById(String productId) {
        System.out.println("productId = " + productId);
        ProductResponse resp = new ProductResponse();
        resp.setId("123");
        return resp;
    }
}
