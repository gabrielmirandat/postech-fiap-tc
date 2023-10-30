package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailData extends ValueObject implements Notifiable {

    @NotBlank(message = "Email address cannot be blank")
    @Email(message = "Invalid email address format")
    private final String email;

    public EmailData(String email) {
        this.email = email;
        validateSelf();
    }
}
