package com.invengest.api_gateway.infraestructure.filter;

import com.invengest.api_gateway.domain.exception.UnauthorizedException;
import com.invengest.api_gateway.domain.model.JwtPayload;
import com.invengest.api_gateway.usecase.ValidateTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final ValidateTokenUseCase validateTokenUseCase;

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLE = "X-User-Role";
    private static final String HEADER_USER_NAME = "X-User-Name";

    // Rutas públicas que no requieren token
    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/api/auth/login"
    );

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Permitir peticiones preflight CORS (OPTIONS) enviadas por el Frontend en Angular
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            return chain.filter(exchange);
        }

        String path = request.getURI().getPath();

        // Omitir validación si la ruta es pública, limpiando posibles headers X-User-* falsificados
        if (OPEN_ENDPOINTS.stream().anyMatch(path::contains)) {
            ServerHttpRequest sanitizedPublicRequest = request.mutate()
                    .headers(headers -> {
                        headers.remove(HEADER_USER_ID);
                        headers.remove(HEADER_USER_ROLE);
                        headers.remove(HEADER_USER_NAME);
                    })
                    .build();
            return chain.filter(exchange.mutate().request(sanitizedPublicRequest).build());
        }

        try {
            // Extraer y validar token mediante el Caso de Uso
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            JwtPayload payload = validateTokenUseCase.execute(authHeader);

            // Inyectar el usuario y rol en los headers para los microservicios downstream (sobrescribiendo cualquier header previo)
            ServerHttpRequest modifiedRequest = request.mutate()
                    .headers(headers -> {
                        headers.set(HEADER_USER_ID, payload.userId());
                        headers.set(HEADER_USER_ROLE, payload.role());
                        headers.set(HEADER_USER_NAME, payload.username());
                    })
                    .build();

            // Continuar con la petición modificada
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (UnauthorizedException ex) {
            // Manejo de errores (Usuarios no Autorizados)
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    // Orden de ejecución del filtro (0 = prioridad alta)
    @Override
    public int getOrder() {
        return 0;
    }
}