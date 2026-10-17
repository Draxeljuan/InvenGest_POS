package com.invengest.transaction.infrastructure.driver_adapter.client.dto;

import java.math.BigDecimal;

public record CatalogProductoDto(
        String idProducto,
        String nombre,
        BigDecimal precioVenta,
        Integer stock,
        Integer stockMinimo
) {}
