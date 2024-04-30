package com.gabriel.permissions.ui.controller.request;

import com.gabriel.permissions.domain.model.Authority;

public record ScopeRequest(String name, String description) {

    public Authority toAuthority() {
        return new Authority(null, name, description, null, null, null);
    }
}
