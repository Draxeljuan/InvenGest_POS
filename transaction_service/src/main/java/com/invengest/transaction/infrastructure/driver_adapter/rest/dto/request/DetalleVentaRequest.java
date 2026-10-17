package com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request;

import com.invengest.transaction.domain.model.TipoItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DetalleVentaRequest(
        @NotBlank(message = "El ID del ítem es obligatorio")
        String idItem,

        @NotNull(message = "El tipo de ítem (PRODUCTO o SERVICIO) es obligatorio")
        TipoItem tipoItem,

        String nombreItem,

        BigDecimal precioUnitario,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser como mínimo 1")
        Integer cantidad
) {}
