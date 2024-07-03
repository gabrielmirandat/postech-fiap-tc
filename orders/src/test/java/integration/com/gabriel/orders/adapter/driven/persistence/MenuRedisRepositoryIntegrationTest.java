package integration.com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.adapter.driven.persistence.MenuRedisRepository;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.infra.redis.RedisConfiguration;
import com.gabriel.orders.infra.serializer.SerializerConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import utils.com.gabriel.orders.adapter.container.RedisTestContainer;
import utils.com.gabriel.orders.core.domain.ExtraMock;
import utils.com.gabriel.orders.core.domain.ProductMock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({RedisConfiguration.class, SerializerConfiguration.class})
@ContextConfiguration(classes = {MenuRedisRepository.class, RedisTestContainer.class})
public class MenuRedisRepositoryIntegrationTest {

    @Autowired
    private MenuRedisRepository menuRepository;
    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;
    private Product product;
    private Extra extra;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        RedisTestContainer.redisProperties(registry);
    }

    @BeforeEach
    void setup() {
        product = ProductMock.validProduct(new ProductID());
        extra = ExtraMock.validExtra(new IngredientID());

        // Clear Redis data
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    void testAddAndGetProduct() {
        menuRepository.addProduct(product);
        Product fetchedProduct = menuRepository.getProduct(product.getProductID());

        assertThat(fetchedProduct).isNotNull();
        assertThat(fetchedProduct.getName().getValue()).isEqualTo(product.getName().getValue());
    }

    @Test
    void testGetNewerProductRightOrder() {
        Product newerProduct = ProductMock.validProduct(new ProductID());
        menuRepository.addProduct(product);
        menuRepository.addProduct(newerProduct);
        Product fetchedProduct = menuRepository.getProduct(newerProduct.getProductID());

        assertThat(fetchedProduct).isNotNull();
        assertEquals(fetchedProduct.getName().getValue(), newerProduct.getName().getValue());
    }

    @Test
    void testGetNewerProductWrongOrder() {
        Product newerProduct = ProductMock.validProduct(new ProductID());
        menuRepository.addProduct(newerProduct);
        menuRepository.addProduct(product);
        Product fetchedProduct = menuRepository.getProduct(product.getProductID());

        assertThat(fetchedProduct).isNotNull();
        assertEquals(fetchedProduct.getName().getValue(), product.getName().getValue());
    }

    @Test
    void testExistsProduct() {
        assertFalse(menuRepository.existsProduct(product.getProductID()));
        menuRepository.addProduct(product);
        assertTrue(menuRepository.existsProduct(product.getProductID()));
    }

    @Test
    void testAllProducts() {
        assertTrue(menuRepository.allProducts().isEmpty());
        menuRepository.addProduct(product);
        assertFalse(menuRepository.allProducts().isEmpty());
        assertEquals(1, menuRepository.allProducts().size());
        assertEquals(product.getProductID(), menuRepository.allProducts().get(0));
    }

    @Test
    void testDeleteProduct() {
        menuRepository.addProduct(product);
        menuRepository.deleteProduct(product.getProductID());
        Product fetchedProduct = menuRepository.getProduct(product.getProductID());

        assertThat(fetchedProduct).isNull();
    }

    @Test
    void testAddAndGetExtra() {
        menuRepository.addExtra(extra);
        Extra fetchedExtra = menuRepository.getExtra(extra.getIngredientID());

        assertThat(fetchedExtra).isNotNull();
        assertThat(fetchedExtra.getName().getValue()).isEqualTo(extra.getName().getValue());
    }

    @Test
    void testExistsExtra() {
        assertFalse(menuRepository.existsExtra(extra.getIngredientID()));
        menuRepository.addExtra(extra);
        assertTrue(menuRepository.existsExtra(extra.getIngredientID()));
    }

    @Test
    void testAllExtras() {
        assertTrue(menuRepository.allExtras().isEmpty());
        menuRepository.addExtra(extra);
        assertFalse(menuRepository.allExtras().isEmpty());
        assertEquals(1, menuRepository.allExtras().size());
        assertEquals(extra.getIngredientID(), menuRepository.allExtras().get(0));
    }

    @Test
    void testDeleteExtra() {
        menuRepository.addExtra(extra);
        menuRepository.deleteExtra(extra.getIngredientID());
        Extra fetchedExtra = menuRepository.getExtra(extra.getIngredientID());

        assertThat(fetchedExtra).isNull();
    }
}
