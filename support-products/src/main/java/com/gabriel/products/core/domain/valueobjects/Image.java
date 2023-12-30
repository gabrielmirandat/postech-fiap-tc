package com.gabriel.products.core.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.products.core.domain.base.ValueObject;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class Image extends ValueObject {

    @Pattern(regexp = ".*\\.png$", message = "Image must wnd with .png")
    private final String url;

    @JsonCreator
    public Image(String url) {
        this.url = url;
        validateSelf();
    }
}
