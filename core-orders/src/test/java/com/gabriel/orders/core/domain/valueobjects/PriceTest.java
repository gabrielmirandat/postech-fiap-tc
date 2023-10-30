package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void shouldCreatePriceSuccessfully() {
        assertDoesNotThrow(() -> new Price(5.0));
    }

    @Test
    void shouldNotCreatePriceWithNegativeValue() {
        Exception exception = assertThrows(DomainException.class, () -> new Price(-1.0));
        assertTrue(exception.getMessage().contains("Price must be at least 0.1"));
    }

    @Test
    void shouldNotCreatePriceWithZeroValue() {
        Exception exception = assertThrows(DomainException.class, () -> new Price(0.0));
        assertTrue(exception.getMessage().contains("Price must be at least 0.1"));
    }

    @Test
    void shouldNotCreatePriceWithTooLowValue() {
        Exception exception = assertThrows(DomainException.class, () -> new Price(0.05));
        assertTrue(exception.getMessage().contains("Price must be at least 0.1"));
    }

    @Test
    void shouldNotCreatePriceWithTooHighValue() {
        Exception exception = assertThrows(DomainException.class, () -> new Price(10000.1));
        assertTrue(exception.getMessage().contains("Price must be less than 10000.0"));
    }
}
