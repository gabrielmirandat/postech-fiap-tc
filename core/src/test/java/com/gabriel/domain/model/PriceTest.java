package com.gabriel.domain.model;

import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void shouldCreatePriceSuccessfully() {
        Assertions.assertDoesNotThrow(() -> new Price(5.0));
    }

    @Test
    void shouldNotCreatePriceWithNegativeValue() {
        Exception exception = Assertions.assertThrows(DomainException.class, () -> new Price(-1.0));
        Assertions.assertTrue(exception.getMessage().contains("Price must be at least 0.1"));
    }

    @Test
    void shouldNotCreatePriceWithZeroValue() {
        Exception exception = Assertions.assertThrows(DomainException.class, () -> new Price(0.0));
        Assertions.assertTrue(exception.getMessage().contains("Price must be at least 0.1"));
    }

    @Test
    void shouldNotCreatePriceWithTooLowValue() {
        Exception exception = Assertions.assertThrows(DomainException.class, () -> new Price(0.05));
        Assertions.assertTrue(exception.getMessage().contains("Price must be at least 0.1"));
    }

    @Test
    void shouldNotCreatePriceWithTooHighValue() {
        Exception exception = Assertions.assertThrows(DomainException.class, () -> new Price(10000.1));
        Assertions.assertTrue(exception.getMessage().contains("Price must be less than 10000.0"));
    }
}
