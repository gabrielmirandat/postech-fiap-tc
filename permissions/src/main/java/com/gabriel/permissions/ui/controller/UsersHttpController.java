package com.gabriel.permissions.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.permissions.ui.controller.request.CredentialsRequest;
import com.gabriel.permissions.ui.controller.request.RegisterRequest;
import com.gabriel.permissions.ui.controller.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;


@RestController
public class UsersHttpController {

    private String issuer;
    private String clientId;
    private String clientSecret;
    private String applicationId;
    private String applicationSecret;
    private String audience;
    private String scope;
    private String applicationScope;
    private String logoutRedirectUrl;
    private ObjectMapper objectMapper;

    public UsersHttpController(
        @Value("${auth0.issuer}") String issuer,
        @Value("${auth0.clientId}") String clientId,
        @Value("${auth0.clientSecret}") String clientSecret,
        @Value("${auth0.applicationId}") String applicationId,
        @Value("${auth0.applicationSecret}") String applicationSecret,
        @Value("${auth0.audience}") String audience,
        @Value("${auth0.scope}") String scope,
        @Value("${auth0.applicationScope}") String applicationScope,
        @Value("${auth0.logout_url}") String logoutRedirectUrl,
        ObjectMapper objectMapper
    ) {
        this.issuer = issuer;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.applicationId = applicationId;
        this.applicationSecret = applicationSecret;
        this.audience = audience;
        this.scope = scope;
        this.applicationScope = applicationScope;
        this.logoutRedirectUrl = logoutRedirectUrl;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsRequest credentialsRequest) {
        try {
            HttpResponse<String> response = Unirest.post("https://" + issuer + "/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("grant_type", "password")
                .field("client_id", clientId)
                .field("client_secret", clientSecret)
                .field("audience", audience)
                .field("scope", scope)
                .field("username", credentialsRequest.username())
                .field("password", credentialsRequest.password())
                .asString();

            if (response.isSuccess()) {
                AuthenticationResponse authenticationResponse = objectMapper.readValue(response.getBody(), AuthenticationResponse.class);
                return ResponseEntity.ok().body(authenticationResponse);
            } else {
                return ResponseEntity.status(response.getStatus()).body("Authentication failed");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server error during authentication");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String logoutUrl = "https://" + issuer + "/v2/logout?client_id=" + clientId + "&returnTo=" + logoutRedirectUrl;
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(logoutUrl)).build();
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            String token = getManagementApiToken();
            HttpResponse<String> response = Unirest.post("https://" + issuer + "/dbconnections/signup")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + token)
                .field("client_id", clientId)
                .field("username", registerRequest.username())
                .field("password", registerRequest.password())
                .field("name", registerRequest.name())
                .field("email", registerRequest.email())
                .field("verify_email", "false")
                .field("connection", "Username-Password-Authentication")
                .asString();


            if (response.isSuccess()) {
                return ResponseEntity.ok("User registered successfully");
            } else {
                return ResponseEntity.status(response.getStatus()).body("Failed to create account: " + response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server error during account creation");
        }
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(Principal principal) {
        try {
            String token = getManagementApiToken();
            HttpResponse<String> response = Unirest.delete("https://" + issuer + "/api/v2/users/" + encodeUserId(principal.getName()))
                .header("Authorization", "Bearer " + token)
                .asString();

            if (response.isSuccess()) {
                return ResponseEntity.ok("Account deleted");
            } else {
                return ResponseEntity.status(response.getStatus()).body("Failed to delete account: " + response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server error during account deletion");
        }
    }

    private String getManagementApiToken() throws UnirestException {
        HttpResponse<String> response = Unirest.post("https://" + issuer + "/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .field("grant_type", "client_credentials")
            .field("client_id", applicationId)
            .field("client_secret", applicationSecret)
            .field("audience", "https://" + issuer + "/api/v2/")
            .field("scope", applicationScope)
            .asString();

        if (response.isSuccess()) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getString("access_token");
        } else {
            throw new RuntimeException("Failed to obtain management API token.");
        }
    }

    private String encodeUserId(String userId) {
        try {
            // Encode the userId using URL encoding rules
            return URLEncoder.encode(userId, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Handle encoding exceptions if necessary
            throw new RuntimeException("Error encoding user ID", e);
        }
    }

}
