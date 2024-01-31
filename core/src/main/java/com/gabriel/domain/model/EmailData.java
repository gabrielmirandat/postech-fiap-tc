package com.gabriel.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.domain.ValueObject;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailData extends ValueObject implements Notifiable {

    @JsonValue
    @NotBlank(message = "Email address cannot be blank")
    @Email(message = "Invalid email address format")
    private final String email;

    public EmailData(String email) {
        this.email = email;
        validateSelf();
    }


    @Override
    public String getValue() {
        return email;
    }
}