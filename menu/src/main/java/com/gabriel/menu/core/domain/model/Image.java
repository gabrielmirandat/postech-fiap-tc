package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.ValueObject;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class Image extends ValueObject {

    @JsonValue
    @Pattern(regexp = ".*\\.png$", message = "Image must end with .png")
    private final String url;

    @JsonCreator
    public Image(String url) {
        this.url = url;
        validateSelf();
    }
}
