package com.gabriel.orders.core.application.usecase;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.specs.menu.MenuItem;
import com.gabriel.specs.menu.MenuResponse;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SetupMenuUseCaseTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private SetupMenuUseCase setupMenuUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetupData() {
        // Given
        // Create a Timestamp for the lastUpdated field
        Timestamp lastUpdated = Timestamp.newBuilder()
            .setSeconds(System.currentTimeMillis() / 1000)
            .setNanos(0)
            .build();

        // Build a MenuItem instance using the Builder
        MenuItem menuItemIngr = MenuItem.newBuilder()
            .setId(new IngredientID().getId())
            .setName("newIngredient")
            .setPrice(9.99)
            .setCategory("burger")
            .setLastUpdated(lastUpdated) // Set the lastUpdated timestamp
            .build();

        // Build a MenuItem instance using the Builder
        MenuItem menuItemProd = MenuItem.newBuilder()
            .setId(new ProductID().getId())
            .setName("newIngredient")
            .setPrice(9.99)
            .setCategory("burger")
            .setLastUpdated(lastUpdated) // Set the lastUpdated timestamp
            .build();

        MenuResponse inputDumpMenuData =
            MenuResponse.newBuilder()
                .addItems(menuItemIngr)
                .addItems(menuItemProd)
                .build();

        // When
        setupMenuUseCase.setupData(inputDumpMenuData);

        // Then
        verify(menuRepository, times(1)).addProduct(any(Product.class));
        verify(menuRepository, times(1)).addExtra(any(Extra.class));
    }
}

