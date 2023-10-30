package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class EmailDataTest {

    @Test
    void shouldThrowExceptionWhenAddressIsBlank() {
        assertThatThrownBy(() -> new EmailData(" "))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Email address cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenAddressDoesNotFollowEmailPattern() {
        assertThatThrownBy(() -> new EmailData("invalid-email"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invalid email address format");

        assertThatThrownBy(() -> new EmailData("invalid@.com"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invalid email address format");
    }

    @Test
    void shouldCreateEmailAddressWhenAddressIsValid() {
        assertThatCode(() -> new EmailData("valid@example.com"))
                .doesNotThrowAnyException();
    }
}

