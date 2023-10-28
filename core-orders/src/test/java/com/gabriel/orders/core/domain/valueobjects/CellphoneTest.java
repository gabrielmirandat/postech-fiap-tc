package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class CellphoneTest {

    @Test
    void shouldThrowExceptionWhenNumberIsBlank() {
        assertThatThrownBy(() -> new Cellphone(" "))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Cellphone number cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenNumberDoesNotFollowPattern() {
        assertThatThrownBy(() -> new Cellphone("(12) 1234-567"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Cellphone number must follow the pattern");

        assertThatThrownBy(() -> new Cellphone("12345678901"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Cellphone number must follow the pattern");
    }

    @Test
    void shouldCreateCellphoneWhenNumberIsValid() {
        assertThatCode(() -> new Cellphone("(12) 1234-5678"))
                .doesNotThrowAnyException();

        assertThatCode(() -> new Cellphone("(12) 12345-6789"))
                .doesNotThrowAnyException();
    }
}

