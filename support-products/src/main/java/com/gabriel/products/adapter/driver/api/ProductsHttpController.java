package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controller.ProductsApi;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.adapter.driver.api.models.ProductCreated;
import com.gabriel.products.adapter.driver.api.models.ProductRequest;
import com.gabriel.products.adapter.driver.api.models.ProductResponse;

public class ProductsHttpController implements ProductsApi {


    @Override
    public ProductCreated addProduct(ProductRequest productRequest) {
        ProductCreated asd = new ProductCreated();
        asd.setProductId("123");
        return asd;
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
