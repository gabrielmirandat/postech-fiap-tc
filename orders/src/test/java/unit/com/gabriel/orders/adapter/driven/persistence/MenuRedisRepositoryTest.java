package unit.com.gabriel.orders.adapter.driven.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.adapter.driven.persistence.MenuRedisRepository;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        ProductID productID = new ProductID();
        when(redisTemplate.hasKey("prod:" + productID.getId())).thenReturn(true);

        boolean exists = repository.existsProduct(productID);

        assertTrue(exists);
        verify(redisTemplate).hasKey("prod:" + productID.getId());
    }

    @Test
    void getProduct_whenProductExists_returnsProduct() throws Exception {
        ProductID productID = new ProductID();
        byte[] serializedProduct = new byte[]{};
        Product product = new Product(productID, "Test Product", 10.0);

        when(valueOperations.get("prod:" + productID.getId())).thenReturn(serializedProduct);
        when(objectMapper.readValue(serializedProduct, Product.class)).thenReturn(product);

        Product result = repository.getProduct(productID);

        assertNotNull(result);
        assertEquals(product.getProductID(), result.getProductID());
        verify(valueOperations).get("prod:" + productID.getId());
        verify(objectMapper).readValue(serializedProduct, Product.class);
    }

    @Test
    void addProduct_savesProduct() throws Exception {
        ProductID productID = new ProductID();
        Product product = new Product(productID, "Test Product", 10.0);
        byte[] serializedProduct = new byte[]{};

        when(objectMapper.writeValueAsBytes(product)).thenReturn(serializedProduct);

        repository.addProduct(product);

        verify(valueOperations).set(eq("prod:" + product.getProductID().getId()), eq(serializedProduct));
    }

    @Test
    void deleteProduct_removesProduct() {
        ProductID productID = new ProductID();
        repository.deleteProduct(productID);
        verify(redisTemplate).delete("prod:" + productID.getId());
    }

    @Test
    void existsExtra_whenExtraExists_returnsTrue() {
        IngredientID ingredientID = new IngredientID();
        when(redisTemplate.hasKey("extr:" + ingredientID.getId())).thenReturn(true);

        boolean exists = repository.existsExtra(ingredientID);

        assertTrue(exists);
        verify(redisTemplate).hasKey("extr:" + ingredientID.getId());
    }

    @Test
    void getExtra_whenExtraExists_returnsExtra() throws Exception {
        IngredientID ingredientID = new IngredientID();
        byte[] serializedExtra = new byte[]{};
        Extra extra = new Extra(ingredientID, "Test Extra", 5.0);

        when(valueOperations.get("extr:" + ingredientID.getId())).thenReturn(serializedExtra);
        when(objectMapper.readValue(serializedExtra, Extra.class)).thenReturn(extra);

        Extra result = repository.getExtra(ingredientID);

        assertNotNull(result);
        assertEquals(extra.getIngredientID(), result.getIngredientID());
        verify(valueOperations).get("extr:" + ingredientID.getId());
        verify(objectMapper).readValue(serializedExtra, Extra.class);
    }

    @Test
    void addExtra_savesExtra() throws Exception {
        IngredientID ingredientID = new IngredientID();
        Extra extra = new Extra(ingredientID, "Test Extra", 5.0);
        byte[] serializedExtra = new byte[]{};

        when(objectMapper.writeValueAsBytes(extra)).thenReturn(serializedExtra);

        repository.addExtra(extra);

        verify(valueOperations).set(eq("extr:" + extra.getIngredientID().getId()), eq(serializedExtra));
    }

    @Test
    void deleteExtra_removesExtra() {
        IngredientID ingredientID = new IngredientID();
        repository.deleteExtra(ingredientID);
        verify(redisTemplate).delete("extr:" + ingredientID.getId());
    }

    @Test
    void allProducts_returnsListOfProductIDs() {
        Set<String> keys = Set.of("prod:5ed5dad3-PRDC-2024-02-12",
            "prod:5ed5dad3-PRDC-2024-03-11");
        when(redisTemplate.keys("prod:*")).thenReturn(keys);

        List<ProductID> productIDs = repository.allProducts();

        assertEquals(2, productIDs.size());
        assertTrue(productIDs.stream().anyMatch(id -> id.getId().equals("5ed5dad3-PRDC-2024-02-12")));
        assertTrue(productIDs.stream().anyMatch(id -> id.getId().equals("5ed5dad3-PRDC-2024-03-11")));
    }

    @Test
    void allExtras_returnsListOfIngredientIDs() {
        Set<String> keys = Set.of("extr:1498994f-INGR-2024-02-11",
            "extr:fabe70b1-INGR-2024-02-12");
        when(redisTemplate.keys("extr:*")).thenReturn(keys);

        List<IngredientID> ingredientIDs = repository.allExtras();

        assertEquals(2, ingredientIDs.size());
        assertTrue(ingredientIDs.stream().anyMatch(id -> id.getId().equals("1498994f-INGR-2024-02-11")));
        assertTrue(ingredientIDs.stream().anyMatch(id -> id.getId().equals("fabe70b1-INGR-2024-02-12")));
    }

}
