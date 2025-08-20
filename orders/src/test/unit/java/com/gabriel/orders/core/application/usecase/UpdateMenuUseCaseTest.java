package com.gabriel.orders.core.application.usecase;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.gabriel.orders.core.domain.ExtraMock;
import com.gabriel.orders.core.domain.ProductMock;

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
        Product product = ProductMock.validProduct(ProductId.newBuilder().setValue("product-123").build());
        updateMenuUseCase.handleProductAdded(product);
        verify(menuRepository).addProduct(product);
    }

    @Test
    void testHandleProductDeleted() {
        Product product = ProductMock.validProduct(ProductId.newBuilder().setValue("product-123").build()); // Populate product with necessary details
        updateMenuUseCase.handleProductDeleted(product);
        verify(menuRepository).deleteProduct(product.getProductId());
    }

    @Test
    void testHandleExtraAdded() {
        Extra extra = ExtraMock.validExtra(IngredientId.newBuilder().setValue("ingredient-123").build()); // Populate extra as necessary
        updateMenuUseCase.handleExtraAdded(extra);
        verify(menuRepository).addExtra(extra);
    }

    @Test
    void testHandleExtraDeleted() {
        Extra extra = ExtraMock.validExtra(IngredientId.newBuilder().setValue("ingredient-123").build()); // Populate extra as necessary
        updateMenuUseCase.handleExtraDeleted(extra);
        verify(menuRepository).deleteExtra(extra.getIngredientId());
    }
}
