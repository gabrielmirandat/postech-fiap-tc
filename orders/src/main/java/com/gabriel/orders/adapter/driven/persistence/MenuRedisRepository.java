package com.gabriel.orders.adapter.driven.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
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
    public boolean existsProduct(ProductId productId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("prod:" + productId.getValue()));
    }

    @Override
    public List<ProductId> allProducts() {
        Set<String> keys = redisTemplate.keys("prod:*");
        if (keys == null)
            return new ArrayList<>();
        return new ArrayList<>(keys).stream()
            .map(key -> ProductId.newBuilder().setValue(key.substring(5)).build())
            .toList();
    }

    @Override
    public Product getProduct(ProductId productId) {
        ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
        byte[] data = valueOps.get("prod:" + productId.getValue());
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
        String key = "prod:" + product.getProductId().getValue();
        Product existingProduct = getProduct(product.getProductId());
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
    public void deleteProduct(ProductId productId) {
        redisTemplate.delete("prod:" + productId.getValue());
    }

    @Override
    public boolean existsExtra(IngredientId ingredientId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("extr:" + ingredientId.getValue()));
    }

    @Override
    public List<IngredientId> allExtras() {
        Set<String> keys = redisTemplate.keys("extr:*");
        if (keys == null)
            return new ArrayList<>();
        return new ArrayList<>(keys).stream()
            .map(key -> IngredientId.newBuilder().setValue(key.substring(5)).build())
            .toList();
    }

    @Override
    public Extra getExtra(IngredientId ingredientId) {
        ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
        byte[] data = valueOps.get("extr:" + ingredientId.getValue());
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
        String key = "extr:" + extra.getIngredientId().getValue();
        Extra existingExtra = getExtra(extra.getIngredientId());
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
    public void deleteExtra(IngredientId ingredientId) {
        redisTemplate.delete("extr:" + ingredientId.getValue());
    }
}
