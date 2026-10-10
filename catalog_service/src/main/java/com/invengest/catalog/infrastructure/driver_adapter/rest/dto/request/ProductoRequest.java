package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductoRequest(
        @NotNull(message = "La categoría es obligatoria") Integer idCategoria,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotNull @DecimalMin("0.0") BigDecimal precioVenta,
        @NotNull @DecimalMin("0.0") BigDecimal costoCompra,
        @NotNull @Min(0) Integer stock,
        @NotNull @Min(0) Integer stockMinimo,
        String ubicacion,
        Integer idEstado
) {}
