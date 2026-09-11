package com.invengest.auth.infrastructure.driver_adapter.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "Formato de correo inválido") String email,
        String telefono,
        @NotBlank(message = "El nombre de usuario es obligatorio") String username,
        @NotBlank(message = "La contraseña es obligatoria") String password,
        @NotNull(message = "El ID del rol es obligatorio (1: Admin, 2: Vendedor)") Integer idRol
) {}
