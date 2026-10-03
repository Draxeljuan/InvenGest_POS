package com.invengest.api_gateway.domain.model;

// Un Record inmutable para transportar los datos del token
public record JwtPayload(String username, String role, String userId) {}
