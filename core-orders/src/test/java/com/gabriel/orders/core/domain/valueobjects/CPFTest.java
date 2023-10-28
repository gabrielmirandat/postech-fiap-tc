package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CPFTest {

    @Test
    void whenIdIsValid_thenNoExceptionIsThrown() {
        // Arrange
        String validCPF = "123.456.789-09";

        // Act
        ThrowableAssert.ThrowingCallable action = () -> new CPF(validCPF);

        // Assert
        assertThatCode(action).doesNotThrowAnyException();
    }

    @Test
    void whenIdIsNull_thenExceptionIsThrown() {
        // Arrange
        String nullCPF = null;

        // Act
        ThrowableAssert.ThrowingCallable action = () -> new CPF(nullCPF);

        // Assert
        assertThatThrownBy(action)
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: id CPF cannot be blank");
    }

    @Test
    void whenIdIsBlank_thenExceptionIsThrown() {
        // Arrange
        String blankCPF = "   ";

        // Act
        ThrowableAssert.ThrowingCallable action = () -> new CPF(blankCPF);

        // Assert
        assertThatThrownBy(action)
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: id CPF cannot be blank, " +
                        "id CPF must follow the pattern XXX.XXX.XXX-XX");
    }

    @Test
    void whenIdDoesNotFollowPattern_thenExceptionIsThrown() {
        // Arrange
        String invalidCPF = "12345678909";

        // Act
        ThrowableAssert.ThrowingCallable action = () -> new CPF(invalidCPF);

        // Assert
        assertThatThrownBy(action)
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: id CPF must follow the pattern XXX.XXX.XXX-XX");
    }
}
