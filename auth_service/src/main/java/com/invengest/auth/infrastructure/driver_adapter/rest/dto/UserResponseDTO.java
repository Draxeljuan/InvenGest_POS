package com.invengest.auth.infrastructure.driver_adapter.rest.dto;

public record UserResponseDTO(
        Integer idUsuario,
        String nombre,
        String apellido,
        String email,
        String username,
        String rol
) {}