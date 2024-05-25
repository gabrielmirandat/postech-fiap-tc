package unit.com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.application.usecase.UpdateMenuUseCase;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.com.gabriel.orders.core.OrderMock;

import static org.mockito.Mockito.verify;

public class UpdateMenuUseCaseTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private UpdateMenuUseCase updateMenuUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleProductAdded() {
        Product product = OrderMock.generateProduct();
        updateMenuUseCase.handleProductAdded(product);
        verify(menuRepository).addProduct(product);
    }

    @Test
    void testHandleProductDeleted() {
        Product product = OrderMock.generateProduct(); // Populate product with necessary details
        updateMenuUseCase.handleProductDeleted(product);
        verify(menuRepository).deleteProduct(product.getProductID());
    }

    @Test
    void testHandleExtraAdded() {
        Extra extra = OrderMock.generateExtra(); // Populate extra as necessary
        updateMenuUseCase.handleExtraAdded(extra);
        verify(menuRepository).addExtra(extra);
    }

    @Test
    void testHandleExtraDeleted() {
        Extra extra = OrderMock.generateExtra(); // Populate extra as necessary
        updateMenuUseCase.handleExtraDeleted(extra);
        verify(menuRepository).deleteExtra(extra.getIngredientID());
    }
}
