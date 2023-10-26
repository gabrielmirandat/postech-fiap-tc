package com.gabriel.products.adapter.driver.api.controllers;

import com.gabriel.products.adapter.driver.api.controllers.models.ProductCategoryDTO;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductCreated;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductRequest;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class ProductsController implements ProductsApi {


    @Override
    public ResponseEntity<ProductCreated> addProduct(ProductRequest productRequest) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String productId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<ProductResponse> findProductsByQuery(Optional<ProductCategoryDTO> category) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(String productId) throws Exception {
        return null;
    }
}
