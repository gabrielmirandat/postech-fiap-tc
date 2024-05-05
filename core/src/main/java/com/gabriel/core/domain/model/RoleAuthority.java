package com.gabriel.core.domain.model;

import com.gabriel.core.domain.ValueObject;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RoleAuthority extends ValueObject {

    @NotBlank(message = "Role name cannot be null or empty")
    private final String roleName;

    @NotBlank(message = "Authority name cannot be null or empty")
    private final String authorityName;

    private final Instant timestamp;

    public RoleAuthority(String roleName, String authorityName, Instant timestamp) {
        this.roleName = roleName;
        this.authorityName = authorityName;
        this.timestamp = timestamp;
        validate();
    }
}
