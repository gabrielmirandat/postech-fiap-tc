package com.gabriel.permissions.ui.controller.request;

import com.gabriel.permissions.domain.model.Role;

public record GroupRequest(String name, String description) {

    public Role toRole() {
        return new Role(null, name, description, null, null, null);
    }
}
