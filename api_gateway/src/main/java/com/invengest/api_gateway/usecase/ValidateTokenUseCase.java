package com.invengest.api_gateway.usecase;

import com.invengest.api_gateway.domain.exception.UnauthorizedException;
import com.invengest.api_gateway.domain.gateway.TokenValidatorGateway;
import com.invengest.api_gateway.domain.model.JwtPayload;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValidateTokenUseCase {

    private final TokenValidatorGateway tokenValidator;

    public JwtPayload execute(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Falta el token de autorización o formato incorrecto");
        }

        String token = authHeader.substring(7);
        return tokenValidator.validateAndExtract(token);
    }
}
