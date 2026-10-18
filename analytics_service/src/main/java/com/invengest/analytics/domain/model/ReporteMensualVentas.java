package com.invengest.analytics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteMensualVentas {
    private int anio;
    private int mes;
    private String nombreMes;
    private String tituloCabecera;
    private List<FilaControlVentas> filas;
    private BigDecimal totalMes;
}
