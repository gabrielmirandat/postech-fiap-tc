package com.gabriel.permissions.ui.controller;

import com.gabriel.permissions.ui.controller.request.CredentialsRequest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CredentialsRequest credentialsRequest) {
        HttpResponse<String> response = Unirest.post("https://" + domain + "/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .field("grant_type", "password")
            .field("client_id", clientId)
            .field("client_secret", clientSecret)
            .field("username", credentialsRequest.username())
            .field("password", credentialsRequest.password())
            .field("audience", audience)
            .field("scope", "openid profile email")
            .asString();

        return ResponseEntity.ok(response.getBody());
    }
}
