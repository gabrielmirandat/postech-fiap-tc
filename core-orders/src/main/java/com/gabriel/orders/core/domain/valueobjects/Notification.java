package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;

public record Notification(NotificationType type, String value) {

    public Notification {
        this.validate(type, value);
    }

    private void validate(NotificationType type, String value) {
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
