package com.gabriel.permissions.domain.model;

import com.gabriel.core.domain.AggregateRoot;
import com.gabriel.core.domain.model.id.PermissionID;
import jakarta.persistence.*;

@Entity
@Table(name = "role_authority")
@AttributeOverrides({
    @AttributeOverride(name = "creationTimestamp", column = @Column(name = "created_at", nullable = false, updatable = false)),
    @AttributeOverride(name = "updateTimestamp", column = @Column(name = "updated_at", nullable = false))
})
public class RoleAuthority extends AggregateRoot {

    public RoleAuthority() {
    }

    public RoleAuthority(RoleAuthorityKey key, PermissionID permissionID, Role role, Authority authority, String userId) {
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
    private PermissionID permissionID;

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

    public RoleAuthorityKey getKey() {
        return key;
    }

    public PermissionID getPermissionID() {
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
}
