package com.gabriel.orders.adapter.driven.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MenuRedisRepository implements MenuRepository {

    private final ObjectMapper objectMapper;

    private final CacheManager cacheManager;

    public MenuRedisRepository(ObjectMapper objectMapper,
                               CacheManager cacheManager) {
        this.objectMapper = objectMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    public boolean existsProduct(ProductID productID) {
        Cache cache = cacheManager.getCache("menu");
        if (cache != null) {
            return cache.get(productID.getId()) != null;
        }
        return false;
    }

    @Override
    public Product getProduct(ProductID productID) {
        Cache cache = cacheManager.getCache("menu");
        if (cache != null) {
            return Product.deserialize(
                objectMapper,
                (byte[]) Objects.requireNonNull(cache.get(productID.getId())).get());
        }
        return null;
    }

    @Override
    public void addProduct(Product product) {
        if (existsProduct(product.getProductID())) {
            Product currentProduct = getProduct(product.getProductID());
            if (currentProduct.getTimestamp().isAfter(product.getTimestamp())) {
                return;
            }
        }
        Objects.requireNonNull(cacheManager.getCache("menu"))
            .put(product.getProductID().getId(), product.serialized(objectMapper));
    }

    @Override
    public void deleteProduct(ProductID productID) {
        Objects.requireNonNull(cacheManager.getCache("menu"))
            .evict(productID.getId());
    }

    @Override
    public boolean existsExtra(IngredientID ingredientID) {
        Cache cache = cacheManager.getCache("menu");
        if (cache != null) {
            return cache.get(ingredientID.getId()) != null;
        }
        return false;
    }

    @Override
    public Extra getExtra(IngredientID ingredientID) {
        Cache cache = cacheManager.getCache("menu");
        if (cache != null) {
            return Extra.deserialize(
                objectMapper,
                (byte[]) Objects.requireNonNull(cache.get(ingredientID.getId())).get());
        }
        return null;
    }

    @Override
    public void addExtra(Extra extra) {
        if (existsExtra(extra.getIngredientID())) {
            Extra currentExtra = getExtra(extra.getIngredientID());
            if (currentExtra.getTimestamp().isAfter(extra.getTimestamp())) {
                return;
            }
        }
        Objects.requireNonNull(cacheManager.getCache("menu"))
            .put(extra.getIngredientID().getId(), extra.serialized(objectMapper));
    }

    @Override
    public void deleteExtra(IngredientID ingredientID) {
        Objects.requireNonNull(cacheManager.getCache("menu"))
            .evict(ingredientID.getId());
    }
}
