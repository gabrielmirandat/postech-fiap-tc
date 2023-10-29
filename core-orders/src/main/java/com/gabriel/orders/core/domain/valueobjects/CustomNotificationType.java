package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CustomNotificationType extends ValueObject implements Notifiable {

    @NotBlank(message = "Custom notification cannot be blank")
    @Pattern(regexp = "[^|]+\\|[^|]+", message = "Custom notification must follow the pattern <vendor>|<value>")
    private final String notification;

    public CustomNotificationType(String notification) {
        this.notification = notification;
        validateSelf();
    }
}
