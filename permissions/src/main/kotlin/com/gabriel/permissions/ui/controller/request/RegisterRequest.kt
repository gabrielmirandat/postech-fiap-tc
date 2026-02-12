package com.gabriel.permissions.ui.controller.request

data class RegisterRequest(
    val username: String,
    val password: String,
    val name: String,
    val email: String
)
