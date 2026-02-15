package com.gabriel.permissions.domain.repository

import com.gabriel.permissions.domain.model.Role
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID

@ApplicationScoped
class RoleRepository : PanacheRepository<Role> {

    fun findById(uuid: UUID): Role? = find("id", uuid).firstResult()

    fun findByName(name: String): Role? = find("name", name).firstResult()

    fun deleteByName(name: String) {
        delete("name", name)
    }

    fun deleteById(uuid: UUID) {
        delete("id", uuid)
    }
}
