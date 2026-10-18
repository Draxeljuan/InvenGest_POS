package com.invengest.transaction.infrastructure.driver_adapter.feign;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SincronizarVentaRequestDto(
        LocalDate fecha,
        BigDecimal totalVenta,
        List<ItemVentaAnalyticsDto> items
) {}
