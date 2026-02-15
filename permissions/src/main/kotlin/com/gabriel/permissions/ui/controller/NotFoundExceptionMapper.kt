package com.gabriel.permissions.ui.controller

import com.gabriel.permissions.domain.model.exceptions.AuthorityNotFoundException
import com.gabriel.permissions.domain.model.exceptions.RoleNotFoundException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class RoleNotFoundExceptionMapper : ExceptionMapper<RoleNotFoundException> {
    override fun toResponse(exception: RoleNotFoundException): Response =
        Response.status(Response.Status.NOT_FOUND).entity(exception.message).build()
}

@Provider
class AuthorityNotFoundExceptionMapper : ExceptionMapper<AuthorityNotFoundException> {
    override fun toResponse(exception: AuthorityNotFoundException): Response =
        Response.status(Response.Status.NOT_FOUND).entity(exception.message).build()
}
