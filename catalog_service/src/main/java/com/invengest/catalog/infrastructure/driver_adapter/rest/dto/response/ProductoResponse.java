package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductoResponse(
        String idProducto,
        CategoriaResponse categoria,
        String nombre,
        BigDecimal precioVenta,
        BigDecimal costoCompra,
        LocalDate fechaIngreso,
        Integer stock,
        Integer stockMinimo,
        String ubicacion,
        EstadoProductoResponse estado
) {}
