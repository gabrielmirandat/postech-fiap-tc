package com.gabriel.permissions.ui.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private OAuth2AuthorizedClientManager authorizedClientManager;

    @GetMapping("/token")
    public String getToken(OAuth2AuthenticationToken authenticationToken, @RequestParam("code") String code) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(
            OAuth2AuthorizeRequest.withClientRegistrationId("okta")
                .principal(authenticationToken)
                .attribute("code", code)
                .build()
        );

        assert authorizedClient != null;
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        return accessToken.getTokenValue();
    }
}
