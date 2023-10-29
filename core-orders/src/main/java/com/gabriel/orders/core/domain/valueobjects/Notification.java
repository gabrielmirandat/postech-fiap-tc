package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Notification extends ValueObject {

    @NotNull(message = "Notification type cannot be null")
    private final NotificationType type;

    @Valid
    private final Notifiable value;

    public Notification(NotificationType type, String value) {
        this.type = type;
        validateSelf();
        this.value = createNotifiable(type, value);
        validateSelf();
    }

    private Notifiable createNotifiable(NotificationType type, String value) {
        return switch (type) {
            case CELLPHONE -> new Cellphone(value);
            case EMAIL -> new EmailAddress(value);
            case CUSTOM -> new CustomNotificationType(value);
        };
    }
}
