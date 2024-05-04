package com.gabriel.core.domain.model;

import com.gabriel.core.domain.ValueObject;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class RoleAuthority extends ValueObject {

    @NotBlank(message = "Role name cannot be null or empty")
    private final String roleName;

    private final List<String> authoritiesNameList;

    public RoleAuthority(String roleName, List<String> authoritiesNameList) {
        this.roleName = roleName;
        this.authoritiesNameList = authoritiesNameList;
        validate();
    }
}
