package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class EmailAddress extends ValueObject {

    @NotBlank(message = "Email address cannot be blank")
    @Email(message = "Invalid email address format")
    private final String address;

    public EmailAddress(String address) {
        this.address = address;
        validateSelf();
    }
}
