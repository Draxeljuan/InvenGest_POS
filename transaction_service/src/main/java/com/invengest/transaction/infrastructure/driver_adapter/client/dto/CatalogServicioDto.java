package com.invengest.transaction.infrastructure.driver_adapter.client.dto;

import java.math.BigDecimal;

public record CatalogServicioDto(
        Integer idServicio,
        String nombre,
        String descripcion,
        BigDecimal precioSugerido
) {}
