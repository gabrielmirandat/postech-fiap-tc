package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Notification extends ValueObject {

    @NotNull(message = "Notification type cannot be null")
    private final NotificationType type;

    @NotBlank(message = "Notification value cannot be blank")
    private final String value;

    public Notification(NotificationType type, String value) {
        this.type = type;
        this.value = value;
        validateSelf();
        validateCustom();
    }

    private void validateCustom() {
        switch (type) {
            case CELLPHONE -> {
                assert value.matches("\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}");
            }
            case EMAIL -> {
                assert value.matches("^[A-Za-z0-9+_.-]+@(.+)$");
            }
            case PUSH_NOTIFICATION -> {
                assert value.matches("^[A-Fa-f0-9]+$");
            }
        }
    }
}
