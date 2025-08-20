package com.gabriel.orders.adapter.driven.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MenuRedisRepositoryTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RedisTemplate<String, byte[]> redisTemplate;

    @Mock
    private ValueOperations<String, byte[]> valueOperations;

    private MenuRedisRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        repository = new MenuRedisRepository(objectMapper, redisTemplate);
    }

    @Test
    void existsProduct_whenProductExists_returnsTrue() {
        ProductId productId = ProductId.newBuilder().setValue("product-123").build();
        when(redisTemplate.hasKey("prod:" + productId.getValue())).thenReturn(true);

        boolean exists = repository.existsProduct(productId);

        assertTrue(exists);
        verify(redisTemplate).hasKey("prod:" + productId.getValue());
    }

    @Test
    void getProduct_whenProductExists_returnsProduct() throws Exception {
        ProductId productId = ProductId.newBuilder().setValue("12345678-PRDC-2024-12-20").build();
        byte[] serializedProduct = new byte[]{};
        Product product = Product.create(productId, "Test Product", 10.0);

        when(valueOperations.get("prod:" + productId.getValue())).thenReturn(serializedProduct);
        when(objectMapper.readValue(serializedProduct, Product.class)).thenReturn(product);

        Product result = repository.getProduct(productId);

        assertNotNull(result);
        assertEquals(product.getProductId(), result.getProductId());
        verify(valueOperations).get("prod:" + productId.getValue());
        verify(objectMapper).readValue(serializedProduct, Product.class);
    }

    @Test
    void addProduct_savesProduct() throws Exception {
        ProductId productId = ProductId.newBuilder().setValue("87654321-PRDC-2024-12-20").build();
        Product product = Product.create(productId, "Test Product", 10.0);
        byte[] serializedProduct = new byte[]{};

        when(objectMapper.writeValueAsBytes(product)).thenReturn(serializedProduct);

        repository.addProduct(product);

        verify(valueOperations).set(eq("prod:" + product.getProductId().getValue()), eq(serializedProduct));
    }

    @Test
    void deleteProduct_removesProduct() {
        ProductId productId = ProductId.newBuilder().setValue("11111111-PRDC-2024-12-20").build();
        repository.deleteProduct(productId);
        verify(redisTemplate).delete("prod:" + productId.getValue());
    }

    @Test
    void existsExtra_whenExtraExists_returnsTrue() {
        IngredientId ingredientID = IngredientId.newBuilder().setValue("22222222-INGR-2024-12-20").build();
        when(redisTemplate.hasKey("extr:" + ingredientID.getValue())).thenReturn(true);

        boolean exists = repository.existsExtra(ingredientID);

        assertTrue(exists);
        verify(redisTemplate).hasKey("extr:" + ingredientID.getValue());
    }

    @Test
    void getExtra_whenExtraExists_returnsExtra() throws Exception {
        IngredientId ingredientID = IngredientId.newBuilder().setValue("33333333-INGR-2024-12-20").build();
        byte[] serializedExtra = new byte[]{};
        Extra extra = Extra.create(ingredientID, "Test Extra", 5.0);

        when(valueOperations.get("extr:" + ingredientID.getValue())).thenReturn(serializedExtra);
        when(objectMapper.readValue(serializedExtra, Extra.class)).thenReturn(extra);

        Extra result = repository.getExtra(ingredientID);

        assertNotNull(result);
        assertEquals(extra.getIngredientId(), result.getIngredientId());
        verify(valueOperations).get("extr:" + ingredientID.getValue());
        verify(objectMapper).readValue(serializedExtra, Extra.class);
    }

    @Test
    void addExtra_savesExtra() throws Exception {
        IngredientId ingredientID = IngredientId.newBuilder().setValue("44444444-INGR-2024-12-20").build();
        Extra extra = Extra.create(ingredientID, "Test Extra", 5.0);
        byte[] serializedExtra = new byte[]{};

        when(objectMapper.writeValueAsBytes(extra)).thenReturn(serializedExtra);

        repository.addExtra(extra);

        verify(valueOperations).set(eq("extr:" + extra.getIngredientId().getValue()), eq(serializedExtra));
    }

    @Test
    void deleteExtra_removesExtra() {
        IngredientId ingredientID = IngredientId.newBuilder().setValue("55555555-INGR-2024-12-20").build();
        repository.deleteExtra(ingredientID);
        verify(redisTemplate).delete("extr:" + ingredientID.getValue());
    }

    @Test
    void allProducts_returnsListOfProductIds() {
        Set<String> keys = Set.of("prod:5ed5dad3-PRDC-2024-02-12", "prod:5ed5dad3-PRDC-2024-03-11");
        when(redisTemplate.keys("prod:*")).thenReturn(keys);

        List<ProductId> productIds = repository.allProducts();

        assertNotNull(productIds);
        assertEquals(2, productIds.size());
        assertTrue(productIds.stream().anyMatch(id -> id.getValue().equals("5ed5dad3-PRDC-2024-02-12")));
        assertTrue(productIds.stream().anyMatch(id -> id.getValue().equals("5ed5dad3-PRDC-2024-03-11")));
    }

    @Test
    void allExtras_returnsListOfIngredientIDs() {
        Set<String> keys = Set.of("extr:1498994f-INGR-2024-02-11", "extr:fabe70b1-INGR-2024-02-12");
        when(redisTemplate.keys("extr:*")).thenReturn(keys);

        List<IngredientId> ingredientIDs = repository.allExtras();

        assertNotNull(ingredientIDs);
        assertEquals(2, ingredientIDs.size());
        assertTrue(ingredientIDs.stream().anyMatch(id -> id.getValue().equals("1498994f-INGR-2024-02-11")));
        assertTrue(ingredientIDs.stream().anyMatch(id -> id.getValue().equals("fabe70b1-INGR-2024-02-12")));
    }
}
