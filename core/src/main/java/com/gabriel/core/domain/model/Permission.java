package com.gabriel.core.domain.model;

import com.gabriel.core.domain.ValueObject;
import com.gabriel.core.domain.model.id.PermissionID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Permission extends ValueObject {

    @Valid
    private final PermissionID permissionID;

    @NotBlank(message = "Role name cannot be null or empty")
    private final String roleName;

    @NotBlank(message = "Authority name cannot be null or empty")
    private final String authorityName;

    private final Instant timestamp;

    public Permission(PermissionID permissionID, String roleName, String authorityName, Instant timestamp) {
        this.permissionID = permissionID;
        this.roleName = roleName;
        this.authorityName = authorityName;
        this.timestamp = timestamp;
        validate();
    }
}
