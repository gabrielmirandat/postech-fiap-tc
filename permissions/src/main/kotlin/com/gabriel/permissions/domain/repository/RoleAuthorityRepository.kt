package com.gabriel.permissions.domain.repository

import com.gabriel.permissions.domain.model.RoleAuthority
import com.gabriel.permissions.domain.model.RoleAuthorityKey
import org.springframework.data.jpa.repository.JpaRepository

interface RoleAuthorityRepository : JpaRepository<RoleAuthority, RoleAuthorityKey>
