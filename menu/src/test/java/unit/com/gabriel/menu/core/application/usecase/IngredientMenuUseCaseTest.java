package unit.com.gabriel.menu.core.application.usecase;

import unit.com.gabriel.menu.core.MenuMock;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.application.usecase.IngredientMenuUseCase;
import com.gabriel.menu.core.application.usecase.IngredientUseCase;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IngredientMenuUseCaseTest {

    @Mock
    IngredientUseCase ingredientUseCase;

    @InjectMocks
    IngredientMenuUseCase ingredientMenuUseCase;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    void setUp() {
        ingredient1 = MenuMock.generateIngredient(true);
        ingredient2 = MenuMock.generateIngredient(false);

        when(ingredientUseCase.searchIngredient(any(SearchIngredientQuery.class)))
            .thenReturn(Arrays.asList(ingredient1, ingredient2));
    }

    @Test
    public void testRetrieveMenuAggregatesIngredientsFromAllCategories() {
        List<Ingredient> ingredients = ingredientMenuUseCase.retrieveMenu();

        int expectedSize = Category.values().length * 2;
        assertEquals(expectedSize, ingredients.size(), "The retrieved ingredients list size should match the expected size.");
    }
}
