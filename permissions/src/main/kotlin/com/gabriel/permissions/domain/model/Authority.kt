package com.gabriel.permissions.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "authority")
open class Authority @JvmOverloads constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    open var id: UUID? = null,

    @Column(nullable = false, unique = true)
    open var name: String? = null,

    @Column(columnDefinition = "TEXT")
    open var description: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    open var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    open var updatedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY)
    @JsonIgnore
    open var roleAuthorities: MutableSet<RoleAuthority> = mutableSetOf()
) {
    override fun toString(): String {
        return "Authority(id=$id, name=$name, description=$description, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
