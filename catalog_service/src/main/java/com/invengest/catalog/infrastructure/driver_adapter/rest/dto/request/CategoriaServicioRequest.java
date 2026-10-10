package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaServicioRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre
) {}
