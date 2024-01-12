package com.gabriel.common.core.domain.model;

import com.gabriel.common.core.domain.base.ValueObject;
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
            case EMAIL -> new EmailData(value);
            case CUSTOM -> new CustomNotification(value);
        };
    }
}
