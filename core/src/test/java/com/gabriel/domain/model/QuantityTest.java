package com.gabriel.domain.model;

import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.Quantity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuantityTest {

    @Test
    public void shouldCreateQuantitySuccessfully_whenSizeIsValid() {
        // Arrange
        int validSize = 5;

        // Act
        Quantity quantity = new Quantity(validSize);

        // Assert
        Assertions.assertNotNull(quantity);
        Assertions.assertEquals(validSize, quantity.getSize());
    }

    @Test
    public void shouldThrowException_whenSizeIsLessThanOne() {
        // Arrange
        int invalidSize = 0;

        // Act & Assert
        Assertions.assertThrows(DomainException.class, () -> new Quantity(invalidSize));
    }

    @Test
    public void shouldThrowException_whenSizeIsMoreThanTen() {
        // Arrange
        int invalidSize = 11;

        // Act & Assert
        Assertions.assertThrows(DomainException.class, () -> new Quantity(invalidSize));
    }

    @Test
    public void shouldCreateQuantitySuccessfully_whenSizeIsExactlyOne() {
        // Arrange
        int validSize = 1;

        // Act
        Quantity quantity = new Quantity(validSize);

        // Assert
        Assertions.assertNotNull(quantity);
        Assertions.assertEquals(validSize, quantity.getSize());
    }

    @Test
    public void shouldCreateQuantitySuccessfully_whenSizeIsExactlyTen() {
        // Arrange
        int validSize = 10;

        // Act
        Quantity quantity = new Quantity(validSize);

        // Assert
        Assertions.assertNotNull(quantity);
        Assertions.assertEquals(validSize, quantity.getSize());
    }
}

