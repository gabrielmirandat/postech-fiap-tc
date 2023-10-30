package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomNotificationTest {

    @Test
    void shouldCreateCustomNotificationTypeSuccessfullyWhenPatternIsCorrect() {
        assertThatCode(() -> new CustomNotification("vendor|value"))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenNotificationIsBlank() {
        assertThatThrownBy(() -> new CustomNotification(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: notification Custom notification cannot be blank, " +
                        "notification Custom notification must follow the pattern <vendor>|<value>");
    }

    @Test
    void shouldThrowExceptionWhenPatternIsIncorrect() {
        assertThatThrownBy(() -> new CustomNotification("invalidPattern"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: notification Custom notification must follow the pattern <vendor>|<value>");
    }

    @Test
    void shouldThrowExceptionWhenNotificationIsNull() {
        assertThatThrownBy(() -> new CustomNotification(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: notification Custom notification cannot be blank");
    }
}

