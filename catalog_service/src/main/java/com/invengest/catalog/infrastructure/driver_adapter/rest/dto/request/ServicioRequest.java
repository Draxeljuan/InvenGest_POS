package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ServicioRequest(
        @NotNull(message = "La categoría del servicio es obligatoria") Integer idCategoriaServicio,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String descripcion,
        @NotNull @DecimalMin("0.0") BigDecimal precioSugerido
) {}
