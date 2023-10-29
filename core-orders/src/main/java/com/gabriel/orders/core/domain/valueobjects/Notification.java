package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Notification extends ValueObject {

    @NotNull(message = "Notification type cannot be null")
    private final NotificationType type;

    @NotBlank(message = "Notification value cannot be blank")
    @Valid
    private final Notifiable value;

    public Notification(NotificationType type, Notifiable value) {
        this.type = type;
        this.value = value;
        validateSelf();
    }
}
