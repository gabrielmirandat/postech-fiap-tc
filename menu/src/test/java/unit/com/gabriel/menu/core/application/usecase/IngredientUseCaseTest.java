package unit.com.gabriel.menu.core.application.usecase;

import com.gabriel.core.domain.model.id.IngredientID;
import unit.com.gabriel.menu.core.MenuMock;
import com.gabriel.menu.core.application.command.CreateIngredientCommand;
import com.gabriel.menu.core.application.query.GetByIngredientIdQuery;
import com.gabriel.menu.core.application.query.GetByIngredientIdsQuery;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.application.usecase.IngredientUseCase;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.port.IngredientPublisher;
import com.gabriel.menu.core.domain.port.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientUseCaseTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientPublisher ingredientPublisher;

    @InjectMocks
    private IngredientUseCase ingredientUseCase;

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        ingredient = MenuMock.generateIngredient(true);
    }

    @Test
    void createIngredientTest() {
        CreateIngredientCommand command = new CreateIngredientCommand("Salt", Category.BURGER, 0.5,
            100.0, true);
        when(ingredientRepository.saveIngredient(any(Ingredient.class))).thenReturn(ingredient);

        Ingredient createdIngredient = ingredientUseCase.createIngredient(command);

        verify(ingredientRepository).saveIngredient(any(Ingredient.class));
        verify(ingredientPublisher).ingredientCreated(any());
        assertNotNull(createdIngredient);
    }

    @Test
    void getIngredientByIdTest() {
        when(ingredientRepository.getById(any())).thenReturn(ingredient);

        IngredientID id = new IngredientID();
        Ingredient result = ingredientUseCase.getIngredientById(new GetByIngredientIdQuery(id));

        verify(ingredientRepository).getById(id);
        assertEquals("anIngredient", result.getName().getValue());
    }

    @Test
    void getIngredientsByIdTest() {
        when(ingredientRepository.getById(any())).thenReturn(ingredient);

        IngredientID id1 = new IngredientID();
        IngredientID id2 = new IngredientID();
        List<Ingredient> results = ingredientUseCase.getIngredientsById(
            new GetByIngredientIdsQuery(Arrays.asList(id1, id2)));

        verify(ingredientRepository, times(2)).getById(any());
        assertEquals(2, results.size());
    }

    @Test
    void searchIngredientTest() {
        when(ingredientRepository.searchBy(any())).thenReturn(Collections.singletonList(ingredient));

        List<Ingredient> results = ingredientUseCase.searchIngredient(new SearchIngredientQuery(Category.BURGER));

        verify(ingredientRepository).searchBy(any());
        assertFalse(results.isEmpty());
    }
}
