package com.invengest.catalog.infrastructure.driver_adapter.feign;

public record SincronizarAlertaStockRequestDto(
        String idProducto,
        String nombreProducto,
        Short stockActual
) {}
