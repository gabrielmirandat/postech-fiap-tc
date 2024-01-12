package com.gabriel.common.core.domain.model;

import com.gabriel.common.core.domain.base.ValueObject;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
