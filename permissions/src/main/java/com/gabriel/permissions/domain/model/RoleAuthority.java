package com.gabriel.permissions.domain.model;

import com.gabriel.core.domain.AggregateRoot;
import com.gabriel.core.domain.model.id.PermissionID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "role_authority")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleAuthority extends AggregateRoot {

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

    @CreationTimestamp
    @Column(name = "associated_at", nullable = false, updatable = false)
    private LocalDateTime associatedAt;

    @Column(name = "user_id", nullable = false)
    private String userId;
}
