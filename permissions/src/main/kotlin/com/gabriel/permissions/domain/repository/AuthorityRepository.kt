package com.gabriel.permissions.domain.repository

import com.gabriel.permissions.domain.model.Authority
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID

@ApplicationScoped
class AuthorityRepository : PanacheRepository<Authority> {

    fun findById(uuid: UUID): Authority? = find("id", uuid).firstResult()

    fun deleteById(uuid: UUID) {
        delete("id", uuid)
    }
}
