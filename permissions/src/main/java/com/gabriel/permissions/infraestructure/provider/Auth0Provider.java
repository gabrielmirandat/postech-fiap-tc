package com.gabriel.permissions.infraestructure.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Auth0Provider {

    private final String issuer;
    private final String applicationId;
    private final String applicationSecret;
    private final String applicationAudience;
    private final String applicationScope;
    private final ObjectMapper objectMapper;

    public Auth0Provider(@Value("${auth0.issuer}") String issuer,
                         @Value("${auth0.applicationId}") String applicationId,
                         @Value("${auth0.applicationSecret}") String applicationSecret,
                         @Value("${auth0.applicationAudience}") String applicationAudience,
                         @Value("${auth0.applicationScope}") String applicationScope,
                         ObjectMapper objectMapper) {
        this.issuer = issuer;
        this.applicationId = applicationId;
        this.applicationSecret = applicationSecret;
        this.applicationAudience = applicationAudience;
        this.applicationScope = applicationScope;
        this.objectMapper = objectMapper;
    }

    public static String formatUserId(String userId) {
        // remove auth0| prefix
        return userId.substring(6);
    }

    public String getManagementApiToken() throws UnirestException {
        HttpResponse<String> response = Unirest.post("https://" + issuer + "/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .field("grant_type", "client_credentials")
            .field("client_id", applicationId)
            .field("client_secret", applicationSecret)
            .field("audience", applicationAudience)
            .field("scope", applicationScope)
            .asString();

        if (response.isSuccess()) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getString("access_token");
        } else {
            throw new RuntimeException("Failed to obtain management API token.");
        }
    }

    public String getRoleIdFromName(String name) {
        String token = getManagementApiToken();
        HttpResponse<String> response = Unirest.get("https://" + issuer + "/api/v2/roles")
            .header("Authorization", "Bearer " + token)
            .queryString("name_filter", name)
            .asString();

        if (response.isSuccess()) {
            JSONArray jsonResponse = new JSONArray(response.getBody());
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject role = jsonResponse.getJSONObject(i);
                if (role.getString("name").equals(name)) {
                    return role.getString("id");
                }
            }
            throw new RuntimeException("No roles found with the exact name: " + name);
        } else {
            throw new RuntimeException("Failed to obtain role id from name. Status: " + response.getStatus());
        }
    }
}
