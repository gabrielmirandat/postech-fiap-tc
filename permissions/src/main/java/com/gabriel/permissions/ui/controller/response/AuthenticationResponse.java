package com.gabriel.permissions.ui.controller.response;

import java.time.Instant;

public record AuthenticationResponse(String accessToken, Instant issuedAt, Instant expiresAt) {
}
