package com.gabriel.permissions.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "role_authority")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleAuthority {

    @Setter
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

    @Column(name = "associated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime associatedAt;

    @Setter
    @Column(name = "user_id", nullable = false)
    private String userId;
}
