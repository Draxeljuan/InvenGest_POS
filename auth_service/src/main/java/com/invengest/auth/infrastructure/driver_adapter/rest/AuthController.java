package com.invengest.auth.infrastructure.driver_adapter.rest;

import com.invengest.auth.infrastructure.driver_adapter.rest.dto.LoginRequestDTO;
import com.invengest.auth.infrastructure.driver_adapter.rest.dto.TokenResponseDTO;
import com.invengest.auth.domain.usecase.LoginUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Inyectamos el Caso de Uso, NO el repositorio
    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        // El orquestador hace el trabajo pesado
        String token = loginUseCase.execute(request.username(), request.password());

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}