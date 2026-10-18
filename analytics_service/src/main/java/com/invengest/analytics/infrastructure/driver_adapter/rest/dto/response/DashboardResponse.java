package com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private LocalDate fechaConsulta;
    private BigDecimal ventasDelDia;
    private Integer operacionesDelDia;
    private BigDecimal ventasAcumuladasPeriodo;
    private List<ResumenVentaResponse> historialVentasRecientes;
    private List<TopProductoResponse> productosMasVendidos;
    private List<AlertaStockResponse> alertasRecientes;
}
