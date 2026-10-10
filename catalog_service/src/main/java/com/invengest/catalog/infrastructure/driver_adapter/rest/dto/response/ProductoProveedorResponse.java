package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response;

import java.math.BigDecimal;

public record ProductoProveedorResponse(
        String idProducto,
        Integer idProveedor,
        BigDecimal precioCompraProveedor
) {}
