package com.gabriel.domain.model;

import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.Cellphone;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CellphoneTest {

    @Test
    void shouldThrowExceptionWhenNumberIsBlank() {
        Assertions.assertThatThrownBy(() -> new Cellphone(" "))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Cellphone number cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenNumberDoesNotFollowPattern() {
        Assertions.assertThatThrownBy(() -> new Cellphone("(12) 1234-567"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Cellphone number must follow the pattern");

        Assertions.assertThatThrownBy(() -> new Cellphone("12345678901"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Cellphone number must follow the pattern");
    }

    @Test
    void shouldCreateCellphoneWhenNumberIsValid() {
        Assertions.assertThatCode(() -> new Cellphone("(12) 1234-5678"))
            .doesNotThrowAnyException();

        Assertions.assertThatCode(() -> new Cellphone("(12) 12345-6789"))
            .doesNotThrowAnyException();
    }
}

