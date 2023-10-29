package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class NotificationTest {

    @Test
    void shouldCreateNotificationSuccessfullyWhenTypeAndValueAreValid() {
        assertThatCode(() -> new Notification(NotificationType.CELLPHONE, "(11) 98765-4321")).doesNotThrowAnyException();
        assertThatCode(() -> new Notification(NotificationType.EMAIL, "example@example.com")).doesNotThrowAnyException();
        assertThatCode(() -> new Notification(NotificationType.CUSTOM, "vendor|value")).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenTypeIsNull() {
        assertThatThrownBy(() -> new Notification(null, "(11) 98765-4321"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Notification type cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenValueIsInvalid() {
        assertThatThrownBy(() -> new Notification(NotificationType.CELLPHONE, "invalid"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: number Cellphone number must follow the pattern (XX) XXXX-XXXX or (XX) XXXXX-XXXX");

        assertThatThrownBy(() -> new Notification(NotificationType.EMAIL, "invalid"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: email Invalid email address format");

        assertThatThrownBy(() -> new Notification(NotificationType.CUSTOM, "invalid"))
                .isInstanceOf(DomainException.class)
                .hasMessage("Domain validation failed: notification Custom notification must follow the pattern <vendor>|<value>");
    }
}



