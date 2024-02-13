package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.MenuMock;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.application.query.SearchMenuQuery;
import com.gabriel.menu.core.application.query.SearchProductQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Menu;
import com.gabriel.menu.core.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuUseCaseTest {

    @Mock
    private IngredientUseCase ingredientUseCase;

    @Mock
    private IngredientMenuUseCase ingredientMenuUseCase;

    @Mock
    private ProductUseCase productUseCase;

    @Mock
    private ProductMenuUseCase productMenuUseCase;

    @InjectMocks
    private MenuUseCase menuUseCase;

    private Ingredient ingredient;

    private Product product;


    @BeforeEach
    void setUp() {
        ingredient = MenuMock.generateIngredient(true);
        product = MenuMock.generateProduct();
    }

    @Test
    void testSearchMenuByCategory() {
        SearchMenuQuery query = new SearchMenuQuery(Category.BURGER);
        List<Ingredient> mockIngredients = List.of(ingredient);
        List<Product> mockProducts = List.of(product);

        when(ingredientUseCase.searchIngredient(any(SearchIngredientQuery.class)))
            .thenReturn(mockIngredients);
        when(productUseCase.searchProduct(any(SearchProductQuery.class)))
            .thenReturn(mockProducts);

        List<Menu> result = menuUseCase.searchMenuByCategory(query);

        verify(ingredientUseCase).searchIngredient(any(SearchIngredientQuery.class));
        verify(productUseCase).searchProduct(any(SearchProductQuery.class));
        assertEquals(2, result.size());
    }

    @Test
    void testSearchMenu() {
        List<Ingredient> mockIngredients = List.of(ingredient);
        List<Product> mockProducts = List.of(product);

        when(ingredientMenuUseCase.retrieveMenu()).thenReturn(mockIngredients);
        when(productMenuUseCase.retrieveMenu()).thenReturn(mockProducts);

        List<Menu> result = menuUseCase.searchMenu();

        verify(ingredientMenuUseCase).retrieveMenu();
        verify(productMenuUseCase).retrieveMenu();
        assertEquals(2, result.size());
    }
}
