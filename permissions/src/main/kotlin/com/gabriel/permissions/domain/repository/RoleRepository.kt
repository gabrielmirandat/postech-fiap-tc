package com.gabriel.permissions.domain.repository

import com.gabriel.permissions.domain.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface RoleRepository : JpaRepository<Role, UUID> {

    fun findByName(name: String): Optional<Role>

    fun deleteByName(name: String)

    override fun deleteAll()
}
