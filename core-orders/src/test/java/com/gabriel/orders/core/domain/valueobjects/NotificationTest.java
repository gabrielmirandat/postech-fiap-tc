package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class NotificationTest {

    @Test
    public void whenValidCellphone_thenNoException() {
        assertDoesNotThrow(() -> new Notification(NotificationType.CELLPHONE, "(12) 34567-8901"));
    }

    @Test
    public void whenInvalidCellphone_thenException() {
        assertThatThrownBy(() -> new Notification(NotificationType.CELLPHONE, "invalid_cellphone"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Notification value must be a valid cellphone number");
    }

    @Test
    public void whenValidEmail_thenNoException() {
        assertDoesNotThrow(() -> new Notification(NotificationType.EMAIL, "example@email.com"));
    }

    @Test
    public void whenInvalidEmail_thenException() {
        assertThatThrownBy(() -> new Notification(NotificationType.EMAIL, "invalid_email"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Notification value must be a valid email address");
    }

    @Test
    public void whenValidPushNotification_thenNoException() {
        assertDoesNotThrow(() -> new Notification(NotificationType.PUSH_NOTIFICATION, "a1B2c3D4"));
    }

    @Test
    public void whenInvalidPushNotification_thenException() {
        assertThatThrownBy(() -> new Notification(NotificationType.PUSH_NOTIFICATION, "invalid_push"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Notification value must be a valid push notification token");
    }

    @Test
    public void whenTypeIsNull_thenException() {
        assertThatThrownBy(() -> new Notification(null, "value"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Notification type cannot be null");
    }

    @Test
    public void whenValueIsBlank_thenException() {
        assertThatThrownBy(() -> new Notification(NotificationType.CELLPHONE, "   "))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Notification value cannot be blank");
    }
}

