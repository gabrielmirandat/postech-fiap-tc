package com.gabriel.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabriel.core.domain.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Address extends ValueObject {

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street name cannot exceed 255 characters")
    private final String street;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 255, message = "City name cannot exceed 255 characters")
    private final String city;

    @NotBlank(message = "State cannot be blank")
    @Size(min = 2, max = 2, message = "State must be exactly 2 characters")
    private final String state;

    @NotBlank(message = "Zip code cannot be blank")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Zip code must follow the pattern XXXXX-XXX")
    private final String zip;

    @JsonCreator
    public Address(@JsonProperty("street") String street, @JsonProperty("city") String city,
                   @JsonProperty("state") String state, @JsonProperty("zip") String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        validate();
    }
}
