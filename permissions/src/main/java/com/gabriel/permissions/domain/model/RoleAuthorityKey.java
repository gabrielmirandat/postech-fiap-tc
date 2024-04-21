package com.gabriel.permissions.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RoleAuthorityKey implements Serializable {
    
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
}
