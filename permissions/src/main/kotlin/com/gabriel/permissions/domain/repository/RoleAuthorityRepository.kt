package com.gabriel.permissions.domain.repository

import com.gabriel.permissions.domain.model.RoleAuthority
import com.gabriel.permissions.domain.model.RoleAuthorityKey
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RoleAuthorityRepository : PanacheRepository<RoleAuthority> {

    fun findById(key: RoleAuthorityKey): RoleAuthority? =
        find("key.roleId = ?1 and key.authorityId = ?2", key.roleId, key.authorityId).firstResult()

    fun deleteById(key: RoleAuthorityKey) {
        delete("key.roleId = ?1 and key.authorityId = ?2", key.roleId, key.authorityId)
    }
}
