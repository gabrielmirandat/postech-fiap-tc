package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class EmailAddressTest {

    @Test
    void shouldThrowExceptionWhenAddressIsBlank() {
        assertThatThrownBy(() -> new EmailAddress(" "))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Email address cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenAddressDoesNotFollowEmailPattern() {
        assertThatThrownBy(() -> new EmailAddress("invalid-email"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invalid email address format");

        assertThatThrownBy(() -> new EmailAddress("invalid@.com"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invalid email address format");
    }

    @Test
    void shouldCreateEmailAddressWhenAddressIsValid() {
        assertThatCode(() -> new EmailAddress("valid@example.com"))
                .doesNotThrowAnyException();
    }
}

