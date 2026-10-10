package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProveedorRequest(
        @NotNull(message = "El estado del proveedor es obligatorio") Integer idEstadoProveedor,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String telefono,
        String email,
        String direccion,
        String nit
) {}
