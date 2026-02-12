package com.gabriel.permissions.domain.model

import com.gabriel.model.PermissionId
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "role_authority")
open class RoleAuthority @JvmOverloads constructor(

    @EmbeddedId
    open var key: RoleAuthorityKey? = null,

    @Convert(converter = PermissionIDConverter::class)
    @Column(name = "permission_id", nullable = false, updatable = false)
    open var permissionID: PermissionId? = null,

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    open var role: Role? = null,

    @ManyToOne
    @MapsId("authorityId")
    @JoinColumn(name = "authority_id")
    open var authority: Authority? = null,

    @Column(name = "user_id", nullable = false)
    open var userId: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    open var createdAt: Instant? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    open var updatedAt: Instant? = null
)
