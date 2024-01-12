package com.gabriel.common.core.domain.model;

import com.gabriel.common.core.domain.base.DomainException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void shouldCreateNotificationSuccessfullyWhenTypeAndValueAreValid() {
        Assertions.assertThatCode(() -> new Notification(NotificationType.CELLPHONE, "(11) 98765-4321")).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> new Notification(NotificationType.EMAIL, "example@example.com")).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> new Notification(NotificationType.CUSTOM, "vendor|value")).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenTypeIsNull() {
        Assertions.assertThatThrownBy(() -> new Notification(null, "(11) 98765-4321"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Notification type cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenValueIsInvalid() {
        Assertions.assertThatThrownBy(() -> new Notification(NotificationType.CELLPHONE, "invalid"))
            .isInstanceOf(DomainException.class)
            .hasMessage("Domain validation failed: number Cellphone number must follow the pattern (XX) XXXX-XXXX or (XX) XXXXX-XXXX");

        Assertions.assertThatThrownBy(() -> new Notification(NotificationType.EMAIL, "invalid"))
            .isInstanceOf(DomainException.class)
            .hasMessage("Domain validation failed: email Invalid email address format");

        Assertions.assertThatThrownBy(() -> new Notification(NotificationType.CUSTOM, "invalid"))
            .isInstanceOf(DomainException.class)
            .hasMessage("Domain validation failed: notification Custom notification must follow the pattern <vendor>|<value>");
    }
}



