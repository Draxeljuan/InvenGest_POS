package com.invengest.auth.infrastructure.security;

import com.invengest.auth.domain.gateway.TokenProviderGateway;
import com.invengest.auth.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProviderAdapter implements TokenProviderGateway {

    // Extraemos el secreto y la expiración desde el application.yml o .env
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Override
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        Instant now = Instant.now();
        Instant expiryInstant = now.plusMillis(jwtExpirationMs);

        return Jwts.builder()
                .subject(user.getNombreUsuario())
                // Agregamos el rol y el ID al payload del token para los otros microservicios
                .claim("role", user.getRol().getNombre())
                .claim("userId", user.getIdUsuario())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryInstant)) // Convertimos Instant a Date para JJWT
                .signWith(key)
                .compact();
    }
}
