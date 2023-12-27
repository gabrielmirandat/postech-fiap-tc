package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controller.ProductsApi;
import com.gabriel.products.adapter.driver.api.mappers.ProductMapper;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.adapter.driver.api.models.ProductCreated;
import com.gabriel.products.adapter.driver.api.models.ProductRequest;
import com.gabriel.products.adapter.driver.api.models.ProductResponse;
import com.gabriel.products.core.application.commands.CreateProductCommand;
import com.gabriel.products.core.application.commands.DeleteProductCommand;
import com.gabriel.products.core.application.queries.GetByProductIdQuery;
import com.gabriel.products.core.application.queries.SearchProductQuery;
import com.gabriel.products.core.application.usecases.CreateProductUseCase;
import com.gabriel.products.core.application.usecases.DeleteProductUseCase;
import com.gabriel.products.core.application.usecases.RetrieveProductUseCase;
import com.gabriel.products.core.application.usecases.SearchProductUseCase;
import com.gabriel.products.core.domain.entities.Product;
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
    public ProductResponse findProductsByQuery(ProductCategoryDTO category) {
        SearchProductQuery query = new SearchProductQuery(category.toString());
        List<Product> products = searchProductUseCase.searchProduct(query);
        return productMapper.toResponse(products.get(0));
    }
}
