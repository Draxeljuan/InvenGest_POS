package com.invengest.analytics.domain.model;

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
public class ResumenVentaDiaria {
    private LocalDate fecha;
    private BigDecimal totalVentas;
    private Integer cantidadOperaciones;
}
