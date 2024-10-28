package com.gabriel.permissions.domain.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class RoleAuthorityKey implements Serializable {

    public RoleAuthorityKey() {
    }

    public RoleAuthorityKey(UUID roleId, UUID authorityId) {
        this.roleId = roleId;
        this.authorityId = authorityId;
    }

    private UUID roleId;     // Matches the RoleAuthority.roleId
    private UUID authorityId; // Matches the RoleAuthority.authorityId

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleAuthorityKey that = (RoleAuthorityKey) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(authorityId, that.authorityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, authorityId);
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public UUID getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(UUID authorityId) {
        this.authorityId = authorityId;
    }
}
