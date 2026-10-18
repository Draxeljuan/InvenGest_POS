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
public class FilaControlVentas {
    private LocalDate fecha;
    private String fechaFormateada;
    private BigDecimal ventasDiarias;
    private BigDecimal acumuladoVentas;
    private String salidas;
    private String controlClases;
    private String notas;
}
