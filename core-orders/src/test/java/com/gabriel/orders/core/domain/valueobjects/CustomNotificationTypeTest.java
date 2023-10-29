package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomNotificationTypeTest {

    @Test
    void shouldCreateCustomNotificationTypeSuccessfullyWhenPatternIsCorrect() {
        assertThatCode(() -> new CustomNotificationType("vendor|value"))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenNotificationIsBlank() {
        assertThatThrownBy(() -> new CustomNotificationType(""))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: notification Custom notification cannot be blank, " +
                        "notification Custom notification must follow the pattern <vendor>|<value>");
    }

    @Test
    void shouldThrowExceptionWhenPatternIsIncorrect() {
        assertThatThrownBy(() -> new CustomNotificationType("invalidPattern"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: notification Custom notification must follow the pattern <vendor>|<value>");
    }

    @Test
    void shouldThrowExceptionWhenNotificationIsNull() {
        assertThatThrownBy(() -> new CustomNotificationType(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: notification Custom notification cannot be blank");
    }
}

