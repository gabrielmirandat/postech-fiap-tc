package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class PriceTest {

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Price cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        assertThatThrownBy(() -> new Price(-10.0))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Price must be positive");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        assertThatThrownBy(() -> new Price(0.0))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Price must be positive");
    }

    @Test
    void shouldCreatePriceWhenValueIsPositive() {
        assertThatCode(() -> new Price(10.0)).doesNotThrowAnyException();
    }
}

