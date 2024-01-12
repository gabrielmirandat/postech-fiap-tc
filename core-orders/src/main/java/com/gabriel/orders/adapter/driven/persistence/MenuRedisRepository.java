package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.common.core.domain.model.id.IngredientID;
import com.gabriel.common.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MenuRedisRepository implements MenuRepository {

    private final CacheManager cacheManager;

    public MenuRedisRepository(CacheManager cacheManager) {
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
    public void addProduct(Product product) {
        Objects.requireNonNull(cacheManager.getCache("menu"))
            .put(product.getProductID().getId(), product);
    }

    @Override
    public void updateProduct(Product product) {
        // TODO: implement
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
    public void addExtra(Extra extra) {
        Objects.requireNonNull(cacheManager.getCache("menu"))
            .put(extra.getIngredientID().getId(), extra);
    }

    @Override
    public void updateExtra(Extra extra) {
        // TODO: implement
    }

    @Override
    public void deleteExtra(IngredientID ingredientID) {
        Objects.requireNonNull(cacheManager.getCache("menu"))
            .evict(ingredientID.getId());
    }
}
