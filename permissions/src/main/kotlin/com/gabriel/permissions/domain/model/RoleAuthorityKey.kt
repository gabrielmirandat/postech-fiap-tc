package com.gabriel.permissions.domain.model

import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class RoleAuthorityKey(
    var roleId: UUID? = null,
    var authorityId: UUID? = null
) : Serializable
