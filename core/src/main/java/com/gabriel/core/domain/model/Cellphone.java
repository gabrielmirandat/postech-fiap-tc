package com.gabriel.core.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class Cellphone extends ValueObject implements Notifiable {

    @JsonValue
    @NotBlank(message = "Cellphone number cannot be blank")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Cellphone number must follow the pattern (XX) XXXX-XXXX or (XX) XXXXX-XXXX")
    private final String number;

    public Cellphone(String number) {
        this.number = number;
        validate();
    }


    @Override
    public String getValue() {
        return number;
    }
}
