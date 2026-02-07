package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Pattern;

public class Image {

    @JsonValue
    @Pattern(regexp = ".*\\.png$", message = "Image must end with .png")
    private final String url;

    @JsonCreator
    public Image(String url) {
        this.url = url;
        validate();
    }

    private void validate() {
        if (url == null || !url.endsWith(".png")) {
            throw new IllegalArgumentException("Image must end with .png");
        }
    }

    public String getUrl() {
        return url;
    }
}
