package com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response;

import java.time.LocalDateTime;

public record MovimientoInventarioResponse(
        Integer idMovimientoInventario,
        String idProducto,
        Integer idUsuario,
        TipoMovimientoResponse tipoMovimiento,
        Integer cantidad,
        LocalDateTime fechaMovimiento,
        String observacion
) {}
