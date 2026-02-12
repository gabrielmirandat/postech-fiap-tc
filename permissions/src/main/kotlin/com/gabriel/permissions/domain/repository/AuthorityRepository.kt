package com.gabriel.permissions.domain.repository

import com.gabriel.permissions.domain.model.Authority
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuthorityRepository : JpaRepository<Authority, UUID>
