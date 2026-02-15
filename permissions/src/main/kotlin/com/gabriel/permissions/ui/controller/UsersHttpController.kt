package com.gabriel.permissions.ui.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabriel.permissions.infraestructure.provider.Auth0Provider
import com.gabriel.permissions.ui.controller.request.CredentialsRequest
import com.gabriel.permissions.ui.controller.request.RegisterRequest
import com.gabriel.permissions.ui.controller.response.AuthenticationResponse
import kong.unirest.core.Unirest
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import jakarta.annotation.security.PermitAll
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UsersHttpController(
    @ConfigProperty(name = "auth0.issuer") private val issuer: String,
    @ConfigProperty(name = "auth0.client-id") private val clientId: String,
    @ConfigProperty(name = "auth0.client-secret") private val clientSecret: String,
    @ConfigProperty(name = "auth0.audience") private val audience: String,
    @ConfigProperty(name = "auth0.scope") private val scope: String,
    @ConfigProperty(name = "auth0.logout-url") private val logoutRedirectUrl: String,
    private val objectMapper: ObjectMapper,
    private val auth0Provider: Auth0Provider
) {

    @POST
    @Path("/login")
    @PermitAll
    fun login(credentialsRequest: CredentialsRequest): Response {
        return try {
            val response = Unirest.post("https://$issuer/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("grant_type", "password")
                .field("client_id", clientId)
                .field("client_secret", clientSecret)
                .field("audience", audience)
                .field("scope", scope)
                .field("username", credentialsRequest.username)
                .field("password", credentialsRequest.password)
                .asString()

            if (response.isSuccess) {
                val authenticationResponse = objectMapper.readValue(response.body, AuthenticationResponse::class.java)
                Response.ok(authenticationResponse).build()
            } else {
                Response.status(response.status).entity("Authentication failed").build()
            }
        } catch (e: Exception) {
            Response.serverError().entity("Server error during authentication").build()
        }
    }

    @POST
    @Path("/logout")
    @PermitAll
    fun logout(): Response {
        val logoutUrl = "https://$issuer/v2/logout?client_id=$clientId&returnTo=$logoutRedirectUrl"
        return Response.seeOther(java.net.URI.create(logoutUrl)).build()
    }

    @POST
    @Path("/register")
    @PermitAll
    fun register(registerRequest: RegisterRequest): Response {
        return try {
            val token = auth0Provider.getManagementApiToken()
            val response = Unirest.post("https://$issuer/dbconnections/signup")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer $token")
                .field("client_id", clientId)
                .field("username", registerRequest.username)
                .field("password", registerRequest.password)
                .field("name", registerRequest.name)
                .field("email", registerRequest.email)
                .field("verify_email", "false")
                .field("connection", "Username-Password-Authentication")
                .asString()

            if (response.isSuccess) {
                Response.ok("User registered successfully").build()
            } else {
                Response.status(response.status).entity("Failed to create account: ${response.body}").build()
            }
        } catch (e: Exception) {
            Response.serverError().entity("Server error during account creation").build()
        }
    }

    @DELETE
    @Path("/delete-account")
    fun deleteAccount(@Context securityContext: SecurityContext): Response {
        return try {
            val principalName = securityContext.userPrincipal?.name ?: return Response.status(Response.Status.UNAUTHORIZED).build()
            val token = auth0Provider.getManagementApiToken()
            val encodedUserId = URLEncoder.encode(principalName, StandardCharsets.UTF_8)
            val response = Unirest.delete("https://$issuer/api/v2/users/$encodedUserId")
                .header("Authorization", "Bearer $token")
                .asString()

            if (response.isSuccess) {
                Response.ok("Account deleted").build()
            } else {
                Response.status(response.status).entity("Failed to delete account: ${response.body}").build()
            }
        } catch (e: Exception) {
            Response.serverError().entity("Server error during account deletion").build()
        }
    }
}
