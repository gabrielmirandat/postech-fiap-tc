package com.gabriel.permissions.ui.controller.request

import com.gabriel.permissions.domain.model.Authority

data class ScopeRequest(
    val name: String,
    val description: String
) {
    fun toAuthority(): Authority = Authority(
        name = name,
        description = description
    )
}
