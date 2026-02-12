package com.gabriel.permissions.infraestructure.security

import com.gabriel.permissions.application.service.PermissionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!test")
class SecurityConfiguration(
    private val permissionService: PermissionService,
    @Value("\${auth0.issuer}") private val issuer: String
) {

    @Bean
    fun web(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/users/login", "/users/register").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }

        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtConverter = JwtAuthenticationConverter()
        jwtConverter.setJwtGrantedAuthoritiesConverter { jwt ->
            val roles = jwt.getClaimAsStringList("postech_roles") ?: emptyList()
            val roleAuthorities = if (roles.isEmpty()) {
                emptySet()
            } else {
                permissionService.retrieveRolesGrantedAuthoritiesByName(roles)
            }

            val scopes = jwt.getClaimAsStringList("scope") ?: emptyList()
            val scopeAuthorities = scopes
                .map { scope -> SimpleGrantedAuthority("SCOPE_$scope") }
                .toSet()

            roleAuthorities + scopeAuthorities
        }

        return jwtConverter
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return JwtDecoders.fromOidcIssuerLocation("https://$issuer/")
    }
}
