package com.gabriel.core.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CustomNotification extends ValueObject implements Notifiable {

    @JsonValue
    @NotBlank(message = "Custom notification cannot be blank")
    @Pattern(regexp = "[^|]+\\|[^|]+", message = "Custom notification must follow the pattern <vendor>|<value>")
    private final String notification;

    public CustomNotification(String notification) {
        this.notification = notification;
        validate();
    }

    @Override
    public String getValue() {
        return notification;
    }
}
