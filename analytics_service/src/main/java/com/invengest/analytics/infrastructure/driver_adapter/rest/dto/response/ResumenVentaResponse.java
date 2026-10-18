package com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenVentaResponse {
    private LocalDate fecha;
    private BigDecimal totalVentas;
    private Integer cantidadOperaciones;
}
