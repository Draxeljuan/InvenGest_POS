package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.gateway.ResumenVentaGateway;
import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.DashboardResumen;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.domain.model.TopProducto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarDashboardUseCaseTest {

    @Mock
    private ResumenVentaGateway resumenVentaGateway;

    @Mock
    private TopProductoGateway topProductoGateway;

    @Mock
    private AlertaStockGateway alertaStockGateway;

    private ConsultarDashboardUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConsultarDashboardUseCase(
                resumenVentaGateway,
                topProductoGateway,
                alertaStockGateway
        );
    }

    @Test
    @DisplayName("Debe consolidar las métricas del día, acumulados, top productos y alertas de stock")
    void debeConsolidarResumenDashboard() {
        LocalDate fechaObjetivo = LocalDate.of(2026, 8, 15);
        LocalDate fechaInicio = fechaObjetivo.minusDays(6);

        ResumenVentaDiaria ventaHoy = ResumenVentaDiaria.builder()
                .fecha(fechaObjetivo)
                .totalVentas(new BigDecimal("95000.00"))
                .cantidadOperaciones(10)
                .build();

        ResumenVentaDiaria ventaAyer = ResumenVentaDiaria.builder()
                .fecha(fechaObjetivo.minusDays(1))
                .totalVentas(new BigDecimal("105000.00"))
                .cantidadOperaciones(14)
                .build();

        TopProducto topProducto = TopProducto.builder()
                .idTop(1)
                .idProducto("PROD-001")
                .nombreProducto("Cuaderno Argollado")
                .cantidadVendida(25)
                .fecha(fechaObjetivo)
                .build();

        AlertaStock alertaStock = AlertaStock.builder()
                .idAlerta(1)
                .idProducto("PROD-002")
                .nombreProducto("Lapicero Negro")
                .stockActual((short) 0)
                .estado("Agotado")
                .fechaAlerta(LocalDateTime.now())
                .build();

        when(resumenVentaGateway.findByFecha(fechaObjetivo)).thenReturn(Optional.of(ventaHoy));
        when(resumenVentaGateway.findByFechaBetween(fechaInicio, fechaObjetivo))
                .thenReturn(List.of(ventaAyer, ventaHoy));
        when(topProductoGateway.findTopNByFechaBetween(fechaInicio, fechaObjetivo, 5))
                .thenReturn(List.of(topProducto));
        when(alertaStockGateway.findRecentAlerts(10))
                .thenReturn(List.of(alertaStock));

        DashboardResumen resultado = useCase.obtenerResumenDashboard(fechaObjetivo, 7);

        assertThat(resultado.getFechaConsulta()).isEqualTo(fechaObjetivo);
        assertThat(resultado.getVentasDelDia()).isEqualByComparingTo("95000.00");
        assertThat(resultado.getOperacionesDelDia()).isEqualTo(10);
        assertThat(resultado.getVentasAcumuladasPeriodo()).isEqualByComparingTo("200000.00");
        assertThat(resultado.getProductosMasVendidos()).hasSize(1);
        assertThat(resultado.getAlertasRecientes()).hasSize(1);
    }
}
