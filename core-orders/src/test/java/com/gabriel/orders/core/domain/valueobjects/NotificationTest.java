package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class NotificationTest {

    @Test
    void shouldCreateNotificationSuccessfullyWhenTypeAndValueAreValid() {
        // Criando objetos notificáveis válidos
        Notifiable cellphone = new Cellphone("(11) 98765-4321");
        Notifiable emailAddress = new EmailAddress("example@example.com");
        Notifiable customNotificationType = new CustomNotificationType("vendor|value");

        // Testando a criação da notificação com os tipos e valores válidos
        assertThatCode(() -> new Notification(NotificationType.CELLPHONE, cellphone)).doesNotThrowAnyException();
        assertThatCode(() -> new Notification(NotificationType.EMAIL, emailAddress)).doesNotThrowAnyException();
        assertThatCode(() -> new Notification(NotificationType.CUSTOM, customNotificationType)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenTypeIsNull() {
        Notifiable cellphone = new Cellphone("(11) 98765-4321");
        assertThatThrownBy(() -> new Notification(null, cellphone))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Notification type cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenValueIsInvalid() {
        // Criando objetos notificáveis inválidos
        Notifiable invalidCellphone = new Cellphone("invalid");
        Notifiable invalidEmailAddress = new EmailAddress("invalid");
        Notifiable invalidCustomNotificationType = new CustomNotificationType("invalid");

        // Testando a criação da notificação com valores inválidos
        assertThatThrownBy(() -> new Notification(NotificationType.CELLPHONE, invalidCellphone))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invalid cellphone format");

        assertThatThrownBy(() -> new Notification(NotificationType.EMAIL, invalidEmailAddress))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invalid email format");

        assertThatThrownBy(() -> new Notification(NotificationType.CUSTOM, invalidCustomNotificationType))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Custom notification must follow the pattern <vendor>|<value>");
    }
}


