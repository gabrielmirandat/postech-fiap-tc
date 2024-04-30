package com.gabriel.permissions.domain.model;

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
public class RoleAuthority {

    @EmbeddedId
    private RoleAuthorityKey id;

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
