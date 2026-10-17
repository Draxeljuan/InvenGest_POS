package com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response;

import com.invengest.transaction.domain.model.TipoItem;

import java.math.BigDecimal;

public record DetalleVentaResponse(
        Integer idDetalleVenta,
        String idItem,
        TipoItem tipoItem,
        String nombreItem,
        BigDecimal precioUnitario,
        Integer cantidad,
        BigDecimal subtotal
) {}
