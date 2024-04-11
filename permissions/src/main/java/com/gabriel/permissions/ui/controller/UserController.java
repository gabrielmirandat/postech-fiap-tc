package com.gabriel.permissions.ui.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("redirect:/oauth2/authorization/okta");
    }

    @GetMapping("/user")
    public String getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            return "Hello, " + oauthToken.getPrincipal().getAttribute("name");
        } else {
            return "Hello, anonymous user";
        }
    }
}
