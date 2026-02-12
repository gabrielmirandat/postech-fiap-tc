package com.gabriel.permissions.ui.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabriel.permissions.infraestructure.provider.Auth0Provider
import com.gabriel.permissions.ui.controller.request.CredentialsRequest
import com.gabriel.permissions.ui.controller.request.RegisterRequest
import com.gabriel.permissions.ui.controller.response.AuthenticationResponse
import jakarta.servlet.http.HttpServletRequest
import kong.unirest.core.Unirest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.Principal

@RestController
@RequestMapping("/users")
class UsersHttpController(
    @Value("\${auth0.issuer}") private val issuer: String,
    @Value("\${auth0.client-id}") private val clientId: String,
    @Value("\${auth0.client-secret}") private val clientSecret: String,
    @Value("\${auth0.audience}") private val audience: String,
    @Value("\${auth0.scope}") private val scope: String,
    @Value("\${auth0.logout-url}") private val logoutRedirectUrl: String,
    private val objectMapper: ObjectMapper,
    private val auth0Provider: Auth0Provider
) {

    @PostMapping("/login")
    fun login(@RequestBody credentialsRequest: CredentialsRequest): ResponseEntity<*> {
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
                val authenticationResponse = objectMapper.readValue(
                    response.body,
                    AuthenticationResponse::class.java
                )
                ResponseEntity.ok().body(authenticationResponse)
            } else {
                ResponseEntity.status(response.status).body("Authentication failed")
            }
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body("Server error during authentication")
        }
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<*> {
        val logoutUrl = "https://$issuer/v2/logout?client_id=$clientId&returnTo=$logoutRedirectUrl"
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(logoutUrl)).build<Any>()
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*> {
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
                ResponseEntity.ok("User registered successfully")
            } else {
                ResponseEntity.status(response.status).body("Failed to create account: ${response.body}")
            }
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body("Server error during account creation")
        }
    }

    @DeleteMapping("/delete-account")
    fun deleteAccount(principal: Principal): ResponseEntity<*> {
        return try {
            val token = auth0Provider.getManagementApiToken()
            val response = Unirest.delete("https://$issuer/api/v2/users/${encodeUserId(principal.name)}")
                .header("Authorization", "Bearer $token")
                .asString()

            if (response.isSuccess) {
                ResponseEntity.ok("Account deleted")
            } else {
                ResponseEntity.status(response.status).body("Failed to delete account: ${response.body}")
            }
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body("Server error during account deletion")
        }
    }

    private fun encodeUserId(userId: String): String {
        return try {
            URLEncoder.encode(userId, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException("Error encoding user Id", e)
        }
    }
}
