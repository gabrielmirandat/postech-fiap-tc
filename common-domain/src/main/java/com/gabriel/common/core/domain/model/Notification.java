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
    private final Notifiable repr;

    public Notification(NotificationType type, String repr) {
        this.type = type;
        validateSelf();
        this.repr = createNotifiable(type, repr);
        validateSelf();
    }

    private Notifiable createNotifiable(NotificationType type, String repr) {
        return switch (type) {
            case CELLPHONE -> new Cellphone(repr);
            case EMAIL -> new EmailData(repr);
            case CUSTOM -> new CustomNotification(repr);
        };
    }
}
