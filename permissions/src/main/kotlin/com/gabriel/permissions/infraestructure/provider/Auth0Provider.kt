package com.gabriel.permissions.infraestructure.provider

import kong.unirest.core.Unirest
import kong.unirest.core.UnirestException
import kong.unirest.core.json.JSONArray
import kong.unirest.core.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Auth0Provider(
    @Value("\${auth0.issuer}") private val issuer: String,
    @Value("\${auth0.app-client-id}") private val appClientId: String,
    @Value("\${auth0.app-client-secret}") private val appClientSecret: String,
    @Value("\${auth0.app-audience}") private val appAudience: String,
    @Value("\${auth0.app-scope}") private val appScope: String
) {

    @Throws(UnirestException::class)
    fun getManagementApiToken(): String {
        val response = Unirest.post("https://$issuer/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .field("grant_type", "client_credentials")
            .field("client_id", appClientId)
            .field("client_secret", appClientSecret)
            .field("audience", appAudience)
            .field("scope", appScope)
            .asString()

        return if (response.isSuccess) {
            val jsonResponse = JSONObject(response.body)
            jsonResponse.getString("access_token")
        } else {
            throw RuntimeException("Failed to obtain management API token.")
        }
    }

    fun getRoleIdFromName(name: String): String {
        val token = getManagementApiToken()
        val response = Unirest.get("https://$issuer/api/v2/roles")
            .header("Authorization", "Bearer $token")
            .queryString("name_filter", name)
            .asString()

        return if (response.isSuccess) {
            val jsonResponse = JSONArray(response.body)
            for (i in 0 until jsonResponse.length()) {
                val role = jsonResponse.getJSONObject(i)
                if (role.getString("name") == name) {
                    return role.getString("id")
                }
            }
            throw RuntimeException("No roles found with the exact name: $name")
        } else {
            throw RuntimeException("Failed to obtain role id from name. Status: ${response.status}")
        }
    }

    companion object {
        @JvmStatic
        fun formatUserId(userId: String): String {
            // remove auth0| prefix
            return userId.substring(6)
        }
    }
}
