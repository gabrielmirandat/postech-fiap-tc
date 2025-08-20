package com.gabriel.orders.core.application.usecase;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.exception.OrderApplicationException;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gabriel.orders.core.application.CreateOrderCommandMock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class VerifyMenuUseCaseTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private VerifyMenuUseCase verifyMenuUseCase;

    private CreateOrderCommand commandWithValidItems;
    private CreateOrderCommand commandWithInvalidProduct;
    private CreateOrderCommand commandWithInvalidExtra;

    private ProductId productIdValid;
    private ProductId productIdInvalid;
    private IngredientId ingredientIdValid;
    private IngredientId ingredientIdInValid;


    @BeforeEach
    void setUp() {
        // Assuming CreateOrderCommand constructor and methods for setting up test data
        productIdValid = ProductId.newBuilder().setValue("product-valid").build();
        ingredientIdValid = IngredientId.newBuilder().setValue("ingredient-valid").build();
        commandWithValidItems = CreateOrderCommandMock.validCommand(productIdValid, ingredientIdValid);

        productIdInvalid = ProductId.newBuilder().setValue("product-invalid").build();
        commandWithInvalidProduct = CreateOrderCommandMock.validCommand(productIdInvalid, ingredientIdValid);

        ingredientIdInValid = IngredientId.newBuilder().setValue("ingredient-invalid").build();
        commandWithInvalidExtra = CreateOrderCommandMock.validCommand(productIdValid, ingredientIdInValid);

        // Configure mocks
        lenient().when(menuRepository.existsProduct(productIdValid)).thenReturn(true);
        lenient().when(menuRepository.existsProduct(productIdInvalid)).thenReturn(false);

        lenient().when(menuRepository.existsExtra(ingredientIdValid)).thenReturn(true);
        lenient().when(menuRepository.existsExtra(ingredientIdInValid)).thenReturn(false);
    }

    @Test
    void validateShouldPassWithValidItems() {
        verifyMenuUseCase.validate(commandWithValidItems);
    }

    @Test
    void validateShouldThrowExceptionWithInvalidProduct() {
        OrderApplicationException exception = assertThrows(
            OrderApplicationException.class,
            () -> verifyMenuUseCase.validate(commandWithInvalidProduct)
        );
    }

    @Test
    void validateShouldThrowExceptionWithInvalidExtra() {
        OrderApplicationException exception = assertThrows(
            OrderApplicationException.class,
            () -> verifyMenuUseCase.validate(commandWithInvalidExtra)
        );
    }
}
