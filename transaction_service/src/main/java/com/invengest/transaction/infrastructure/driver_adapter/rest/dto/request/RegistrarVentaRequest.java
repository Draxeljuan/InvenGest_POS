package com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegistrarVentaRequest(
        @NotNull(message = "El ID del usuario es obligatorio")
        Integer idUsuario,

        Integer idCliente,

        @NotNull(message = "El ID del método de pago es obligatorio")
        Integer idMetodoPago,

        @NotEmpty(message = "La lista de ítems a vender no puede estar vacía")
        List<@Valid DetalleVentaRequest> items
) {}
