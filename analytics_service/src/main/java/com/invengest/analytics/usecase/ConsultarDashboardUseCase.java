package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.exception.BusinessRuleException;
import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.gateway.ResumenVentaGateway;
import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.DashboardResumen;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.domain.model.TopProducto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ConsultarDashboardUseCase {

    private static final int DIAS_HISTORIAL_POR_DEFECTO = 7;
    private static final int LIMITE_TOP_PRODUCTOS = 5;
    private static final int LIMITE_ALERTAS_RECIENTES = 10;

    private final ResumenVentaGateway resumenVentaGateway;
    private final TopProductoGateway topProductoGateway;
    private final AlertaStockGateway alertaStockGateway;

    public ConsultarDashboardUseCase(
            ResumenVentaGateway resumenVentaGateway,
            TopProductoGateway topProductoGateway,
            AlertaStockGateway alertaStockGateway) {
        this.resumenVentaGateway = resumenVentaGateway;
        this.topProductoGateway = topProductoGateway;
        this.alertaStockGateway = alertaStockGateway;
    }

    public DashboardResumen obtenerResumenDashboard(LocalDate fechaReferencia, Integer diasHistorial) {
        LocalDate fechaObjetivo = fechaReferencia != null ? fechaReferencia : LocalDate.now();
        int dias = (diasHistorial != null && diasHistorial > 0) ? diasHistorial : DIAS_HISTORIAL_POR_DEFECTO;

        LocalDate fechaInicioPeriodo = fechaObjetivo.minusDays(dias - 1L);

        Optional<ResumenVentaDiaria> resumenHoy = resumenVentaGateway.findByFecha(fechaObjetivo);
        BigDecimal ventasDelDia = resumenHoy.map(ResumenVentaDiaria::getTotalVentas).orElse(BigDecimal.ZERO);
        Integer operacionesDelDia = resumenHoy.map(ResumenVentaDiaria::getCantidadOperaciones).orElse(0);

        List<ResumenVentaDiaria> historialVentas = resumenVentaGateway.findByFechaBetween(fechaInicioPeriodo, fechaObjetivo);

        BigDecimal ventasAcumuladasPeriodo = historialVentas.stream()
                .map(ResumenVentaDiaria::getTotalVentas)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<TopProducto> topProductos = topProductoGateway.findTopNByFechaBetween(
                fechaInicioPeriodo, fechaObjetivo, LIMITE_TOP_PRODUCTOS);

        List<AlertaStock> alertasRecientes = alertaStockGateway.findRecentAlerts(LIMITE_ALERTAS_RECIENTES);

        return DashboardResumen.builder()
                .fechaConsulta(fechaObjetivo)
                .ventasDelDia(ventasDelDia)
                .operacionesDelDia(operacionesDelDia)
                .ventasAcumuladasPeriodo(ventasAcumuladasPeriodo)
                .historialVentasRecientes(historialVentas)
                .productosMasVendidos(topProductos)
                .alertasRecientes(alertasRecientes)
                .build();
    }

    public List<ResumenVentaDiaria> consultarVentasPorRango(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            throw new BusinessRuleException("Las fechas de inicio y fin son obligatorias.");
        }
        if (inicio.isAfter(fin)) {
            throw new BusinessRuleException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        return resumenVentaGateway.findByFechaBetween(inicio, fin);
    }
}
