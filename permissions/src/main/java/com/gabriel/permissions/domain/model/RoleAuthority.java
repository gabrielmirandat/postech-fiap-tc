package com.gabriel.permissions.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "role_authority")
@IdClass(RoleAuthorityKey.class)  // This should be linked with simple fields below
public class RoleAuthority {

    @Id
    @Column(name = "role_id")
    private Long roleId;  // Corresponds to roleId in RoleAuthorityKey

    @Id
    @Column(name = "authority_id")
    private Long authorityId;  // Corresponds to authorityId in RoleAuthorityKey

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "authority_id", insertable = false, updatable = false)
    private Authority authority;

    @Column(name = "associated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime associatedAt;

    @Column(name = "user_id", nullable = false)
    private String userId;
}
