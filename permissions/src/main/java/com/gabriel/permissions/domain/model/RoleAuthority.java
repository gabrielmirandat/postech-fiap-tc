package com.gabriel.permissions.domain.model;

import com.gabriel.model.PermissionId;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "role_authority")
public class RoleAuthority {

    public RoleAuthority() {
    }

    public RoleAuthority(RoleAuthorityKey key, PermissionId permissionID, Role role, Authority authority, String userId) {
        this.key = key;
        this.permissionID = permissionID;
        this.role = role;
        this.authority = authority;
        this.userId = userId;
    }

    @EmbeddedId
    private RoleAuthorityKey key;

    @Convert(converter = PermissionIDConverter.class)
    @Column(name = "permission_id", nullable = false, updatable = false)
    private PermissionId permissionID;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @MapsId("authorityId")
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public RoleAuthorityKey getKey() {
        return key;
    }

    public PermissionId getPermissionID() {
        return permissionID;
    }

    public Role getRole() {
        return role;
    }

    public Authority getAuthority() {
        return authority;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
