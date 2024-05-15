package com.gabriel.orders.adapter.driven.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class MenuRedisRepository implements MenuRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, byte[]> redisTemplate;

    public MenuRedisRepository(ObjectMapper objectMapper,
                               RedisTemplate<String, byte[]> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    private void showCurrentKeys() {
        System.out.println("Current keys:");
        Objects.requireNonNull(redisTemplate.keys("prod:*"))
            .forEach(System.out::println);
        Objects.requireNonNull(redisTemplate.keys("extr:*"))
            .forEach(System.out::println);
    }

    @Override
    public boolean existsProduct(ProductID productID) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("prod:" + productID.getId()));
    }

    @Override
    public List<ProductID> allProducts() {
        Set<String> keys = redisTemplate.keys("prod:*");
        if (keys == null)
            return new ArrayList<>();
        return new ArrayList<>(keys).stream()
            .map(key -> new ProductID(key.substring(5)))
            .toList();
    }

    @Override
    public Product getProduct(ProductID productID) {
        ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
        byte[] data = valueOps.get("prod:" + productID.getId());
        if (data != null) {
            try {
                return objectMapper.readValue(data, Product.class);
            } catch (IOException e) {
                throw new IllegalStateException("Error deserializing product", e);
            }
        }
        return null;
    }

    @Override
    public void addProduct(Product product) {
        String key = "prod:" + product.getProductID().getId();
        Product existingProduct = getProduct(product.getProductID());
        if (existingProduct == null || product.getTimestamp().isAfter(existingProduct.getTimestamp())) {
            ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
            try {
                byte[] serializedProduct = objectMapper.writeValueAsBytes(product);
                valueOps.set(key, serializedProduct);
            } catch (IOException e) {
                throw new IllegalStateException("Error serializing product", e);
            }
        }
        showCurrentKeys();
    }

    @Override
    public void deleteProduct(ProductID productID) {
        redisTemplate.delete("prod:" + productID.getId());
    }

    @Override
    public boolean existsExtra(IngredientID ingredientID) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("extr:" + ingredientID.getId()));
    }

    @Override
    public List<IngredientID> allExtras() {
        Set<String> keys = redisTemplate.keys("extr:*");
        if (keys == null)
            return new ArrayList<>();
        return new ArrayList<>(keys).stream()
            .map(key -> new IngredientID(key.substring(5)))
            .toList();
    }

    @Override
    public Extra getExtra(IngredientID ingredientID) {
        ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
        byte[] data = valueOps.get("extr:" + ingredientID.getId());
        if (data != null) {
            try {
                return objectMapper.readValue(data, Extra.class);
            } catch (IOException e) {
                throw new IllegalStateException("Error deserializing extra", e);
            }
        }
        return null;
    }

    @Override
    public void addExtra(Extra extra) {
        String key = "extr:" + extra.getIngredientID().getId();
        Extra existingExtra = getExtra(extra.getIngredientID());
        if (existingExtra == null || extra.getTimestamp().isAfter(existingExtra.getTimestamp())) {
            ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
            try {
                byte[] serializedExtra = objectMapper.writeValueAsBytes(extra);
                valueOps.set(key, serializedExtra);
            } catch (IOException e) {
                throw new IllegalStateException("Error serializing extra", e);
            }
        }
        showCurrentKeys();
    }

    @Override
    public void deleteExtra(IngredientID ingredientID) {
        redisTemplate.delete("extr:" + ingredientID.getId());
    }
}
