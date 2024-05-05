package com.gabriel.core.domain.model.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.EntityType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PermissionID extends EntityID {

    @JsonValue
    @Pattern(regexp = "[0-9a-f]{8}-PERM-\\d{4}-\\d{2}-\\d{2}",
        message = "Invalid Permission ID format")
    private final String id;

    public PermissionID() {
        this.id = generate(EntityType.PERMISSION);
        validate();
    }

    public PermissionID(String id) {
        this.id = id;
        validate();
    }
}
