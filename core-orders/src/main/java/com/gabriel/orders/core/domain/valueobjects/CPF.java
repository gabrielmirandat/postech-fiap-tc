package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CPF extends ValueObject {

    @NotBlank(message = "CPF cannot be blank")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must follow the pattern XXX.XXX.XXX-XX")
    private final String id;

    public CPF(String id) {
        this.id = id;
        validateSelf();
    }
}
