package com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimientoInventarioRequest(
        @NotBlank(message = "El ID del producto es obligatorio")
        String idProducto,

        @NotNull(message = "El ID del usuario es obligatorio")
        Integer idUsuario,

        @NotNull(message = "El ID del tipo de movimiento es obligatorio")
        Integer idTipoMovimiento,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser como mínimo 1")
        Integer cantidad,

        String observacion
) {}
