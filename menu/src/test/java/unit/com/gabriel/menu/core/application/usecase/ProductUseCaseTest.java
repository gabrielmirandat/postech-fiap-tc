package unit.com.gabriel.menu.core.application.usecase;

import unit.com.gabriel.menu.core.MenuMock;
import com.gabriel.menu.core.application.command.CreateProductCommand;
import com.gabriel.menu.core.application.command.DeleteProductCommand;
import com.gabriel.menu.core.application.query.*;
import com.gabriel.menu.core.application.usecase.IngredientUseCase;
import com.gabriel.menu.core.application.usecase.ProductUseCase;
import com.gabriel.menu.core.domain.event.ProductCreatedEvent;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.menu.core.domain.port.ProductPublisher;
import com.gabriel.menu.core.domain.port.ProductRepository;
import com.gabriel.menu.core.domain.port.SearchParameters;
import com.gabriel.specs.menu.models.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IngredientUseCase ingredientUseCase;

    @Mock
    private ProductPublisher productPublisher;

    @InjectMocks
    private ProductUseCase productUseCase;

    private Product product1;
    private Product product2;

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        product1 = MenuMock.generateProduct();
        product2 = MenuMock.generateProduct();
        ingredient = MenuMock.generateIngredient(false);
    }

    @Test
    void createProductSuccessfully() {
        CreateProductCommand command = new CreateProductCommand(
            "aProduct", 1.0, Category.BURGER, "aDescription", "image.png",
            List.of(ingredient.getIngredientID()));
        when(productRepository.saveProduct(any(Product.class))).thenReturn(product1);
        // Assume searchIngredient returns a list of IngredientIDs
        when(ingredientUseCase.searchIngredient(any(SearchIngredientQuery.class)))
            .thenReturn(List.of(ingredient));

        Product createdProduct = productUseCase.createProduct(command);

        assertNotNull(createdProduct);
        verify(productRepository).saveProduct(any(Product.class));
        verify(productPublisher).productCreated(any(ProductCreatedEvent.class));
    }

    @Test
    void deleteProductSuccessfully() {
        DeleteProductCommand command = new DeleteProductCommand(null);

        assertDoesNotThrow(() -> productUseCase.deleteProduct(command));
        verify(productRepository).deleteProduct(command.deleteId());
    }

    @Test
    void getProductByIdSuccessfully() {
        GetByProductIdQuery query = new GetByProductIdQuery(null);
        when(productRepository.getById(query.searchId())).thenReturn(product1);

        Product result = productUseCase.getProductById(query);

        assertNotNull(result);
        verify(productRepository).getById(query.searchId());
    }

    @Test
    void getResponseByProductSuccessfully() {
        GetByProductQuery query = new GetByProductQuery(product1);
        // Assume getIngredientsById returns a list of Ingredients
        when(ingredientUseCase.getIngredientsById(any(GetByIngredientIdsQuery.class)))
            .thenReturn(List.of(ingredient));

        ProductResponse response = productUseCase.getResponseByProduct(query);

        assertNotNull(response);
    }

    @Test
    void searchProductSuccessfully() {
        SearchProductQuery query = new SearchProductQuery(Category.BURGER);
        List<Product> expectedProducts = List.of(product1, product2);

        when(productRepository.searchBy(any(SearchParameters.class))).thenReturn(expectedProducts);

        List<Product> resultProducts = productUseCase.searchProduct(query);

        assertNotNull(resultProducts);
        assertFalse(resultProducts.isEmpty());
        assertEquals(expectedProducts.size(), resultProducts.size());
        verify(productRepository).searchBy(any(SearchParameters.class));
    }

    @Test
    void searchProductResponseSuccessfully() {
        SearchProductQuery query = new SearchProductQuery(Category.BURGER);
        List<Product> products = List.of(product1, product2);
        List<Ingredient> ingredients = List.of(ingredient);

        when(productRepository.searchBy(any(SearchParameters.class))).thenReturn(products);
        when(ingredientUseCase.getIngredientsById(any(GetByIngredientIdsQuery.class))).thenReturn(ingredients);

        List<ProductResponse> resultResponses = productUseCase.searchProductResponse(query);

        assertNotNull(resultResponses);
        assertFalse(resultResponses.isEmpty());
        assertEquals(products.size(), resultResponses.size());
    }

}
