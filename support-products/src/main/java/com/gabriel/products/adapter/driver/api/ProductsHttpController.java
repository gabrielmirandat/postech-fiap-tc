package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controllers.ProductsApi;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductCategoryDTO;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductCreated;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductRequest;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductResponse;

public class ProductsHttpController implements ProductsApi {


    @Override
    public ProductCreated addProduct(ProductRequest productRequest) {
        return null;
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
        return null;
    }
}
