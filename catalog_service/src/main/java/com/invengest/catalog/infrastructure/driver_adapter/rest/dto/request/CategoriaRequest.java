package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String descripcion
) {}
