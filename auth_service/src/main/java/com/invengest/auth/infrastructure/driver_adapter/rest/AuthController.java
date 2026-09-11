package com.invengest.auth.infrastructure.driver_adapter.rest;

import com.invengest.auth.domain.model.Role;
import com.invengest.auth.domain.model.User;
import com.invengest.auth.domain.usecase.RegisterUserUseCase;
import com.invengest.auth.infrastructure.driver_adapter.rest.dto.LoginRequestDTO;
import com.invengest.auth.infrastructure.driver_adapter.rest.dto.RegisterUserRequestDTO;
import com.invengest.auth.infrastructure.driver_adapter.rest.dto.TokenResponseDTO;
import com.invengest.auth.domain.usecase.LoginUseCase;
import com.invengest.auth.infrastructure.driver_adapter.rest.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Inyectamos los casos de uso
    private final LoginUseCase loginUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        // El orquestador hace el trabajo pesado
        String token = loginUseCase.execute(request.username(), request.password());

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO request) {

        // Mapeo manual básico (podrías usar MapStruct para el DTO a Domain)
        User newUser = new User();
        newUser.setNombre(request.nombre());
        newUser.setApellido(request.apellido());
        newUser.setEmail(request.email());
        newUser.setTelefono(request.telefono());
        newUser.setNombreUsuario(request.username());
        newUser.setRol(new Role(request.idRol(), null));

        // Ejecutar el caso de uso
        User savedUser = registerUserUseCase.execute(newUser, request.password());

        // Armar la respuesta segura
        UserResponseDTO response = new UserResponseDTO(
                savedUser.getIdUsuario(),
                savedUser.getNombre(),
                savedUser.getApellido(),
                savedUser.getEmail(),
                savedUser.getNombreUsuario(),
                savedUser.getRol() != null ? savedUser.getRol().getNombre() : "Rol ID: " + request.idRol()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}