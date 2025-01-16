package com.gabriel.permissions.infraestructure.provider;

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
    private final String appClientId;
    private final String appClientSecret;
    private final String appAudience;
    private final String appScope;

    public Auth0Provider(@Value("${auth0.issuer}") String issuer,
                         @Value("${auth0.app-client-id}") String appClientId,
                         @Value("${auth0.app-client-secret}") String appClientSecret,
                         @Value("${auth0.app-audience}") String appAudience,
                         @Value("${auth0.app-scope}") String appScope) {
        this.issuer = issuer;
        this.appClientId = appClientId;
        this.appClientSecret = appClientSecret;
        this.appAudience = appAudience;
        this.appScope = appScope;
    }

    public static String formatUserId(String userId) {
        // remove auth0| prefix
        return userId.substring(6);
    }

    public String getManagementApiToken() throws UnirestException {
        HttpResponse<String> response = Unirest.post("https://" + issuer + "/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .field("grant_type", "client_credentials")
            .field("client_id", appClientId)
            .field("client_secret", appClientSecret)
            .field("audience", appAudience)
            .field("scope", appScope)
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
