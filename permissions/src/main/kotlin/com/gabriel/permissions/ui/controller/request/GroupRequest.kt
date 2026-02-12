package com.gabriel.permissions.ui.controller.request

import com.gabriel.permissions.domain.model.Role

data class GroupRequest(
    val name: String,
    val description: String
) {
    fun toRole(): Role = Role(
        name = name,
        description = description
    )
}
