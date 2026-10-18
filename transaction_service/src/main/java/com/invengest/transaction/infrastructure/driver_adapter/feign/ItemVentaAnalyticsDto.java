package com.invengest.transaction.infrastructure.driver_adapter.feign;

public record ItemVentaAnalyticsDto(
        String idProducto,
        String nombreProducto,
        Integer cantidadVendida
) {}
