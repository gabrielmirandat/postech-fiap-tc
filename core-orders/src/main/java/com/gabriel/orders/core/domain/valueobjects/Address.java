package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Address extends ValueObject {

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street name cannot exceed 255 characters")
    private String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 255, message = "City name cannot exceed 255 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(min = 2, max = 2, message = "State must be exactly 2 characters")
    private String state;

    @NotBlank(message = "Zip code cannot be blank")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Zip code must follow the pattern XXXXX-XXX")
    private String zip;
}
