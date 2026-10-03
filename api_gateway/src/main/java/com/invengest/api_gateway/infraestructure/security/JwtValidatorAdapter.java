package com.invengest.api_gateway.infraestructure.security;

import com.invengest.api_gateway.domain.exception.UnauthorizedException;
import com.invengest.api_gateway.domain.gateway.TokenValidatorGateway;
import com.invengest.api_gateway.domain.model.JwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtValidatorAdapter implements TokenValidatorGateway {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public JwtPayload validateAndExtract(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return new JwtPayload(
                    claims.getSubject(),
                    claims.get("role", String.class),
                    String.valueOf(claims.get("userId"))
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Token inválido o expirado");
        }
    }
}