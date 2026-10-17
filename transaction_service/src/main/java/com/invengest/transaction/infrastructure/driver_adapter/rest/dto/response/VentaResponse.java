package com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VentaResponse(
        Integer idVenta,
        Integer idUsuario,
        LocalDateTime fecha,
        ClienteResponse cliente,
        MetodoPagoResponse metodoPago,
        BigDecimal total,
        List<DetalleVentaResponse> detalles
) {}
