package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.exception.BusinessRuleException;
import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.gateway.ReporteExcelGateway;
import com.invengest.analytics.domain.gateway.ReportePdfGateway;
import com.invengest.analytics.domain.gateway.ResumenVentaGateway;
import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.domain.model.FilaControlVentas;
import com.invengest.analytics.domain.model.ReporteMensualVentas;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerarReporteControlVentasUseCaseTest {

    @Mock
    private ResumenVentaGateway resumenVentaGateway;

    @Mock
    private TopProductoGateway topProductoGateway;

    @Mock
    private AlertaStockGateway alertaStockGateway;

    @Mock
    private ReporteExcelGateway reporteExcelGateway;

    @Mock
    private ReportePdfGateway reportePdfGateway;

    private GenerarReporteControlVentasUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GenerarReporteControlVentasUseCase(
                resumenVentaGateway,
                topProductoGateway,
                alertaStockGateway,
                reporteExcelGateway,
                reportePdfGateway
        );
    }

    @Test
    @DisplayName("Debe iterar los 31 días de Agosto 2026 y calcular el Acumulado Ventas según RF05")
    void debeConstruirReporteMensualConAcumuladoCorrecto() {
        int anio = 2026;
        int mes = 8;

        List<ResumenVentaDiaria> ventasRegistradas = List.of(
                ResumenVentaDiaria.builder()
                        .fecha(LocalDate.of(2026, 8, 1))
                        .totalVentas(new BigDecimal("150000.00"))
                        .cantidadOperaciones(12)
                        .build(),
                ResumenVentaDiaria.builder()
                        .fecha(LocalDate.of(2026, 8, 3))
                        .totalVentas(new BigDecimal("200000.00"))
                        .cantidadOperaciones(18)
                        .build(),
                ResumenVentaDiaria.builder()
                        .fecha(LocalDate.of(2026, 8, 31))
                        .totalVentas(new BigDecimal("50000.00"))
                        .cantidadOperaciones(5)
                        .build()
        );

        when(resumenVentaGateway.findByFechaBetween(
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 31)))
                .thenReturn(ventasRegistradas);

        ReporteMensualVentas reporte = useCase.construirDatosReporteMensual(anio, mes);

        assertThat(reporte.getTituloCabecera()).isEqualTo("\"VARIEDADES MARELIZA\" AGOSTO 2.026");
        assertThat(reporte.getFilas()).hasSize(31);
        assertThat(reporte.getTotalMes()).isEqualByComparingTo(new BigDecimal("400000.00"));

        // Día 1: Venta 150.000, Acumulado 150.000
        FilaControlVentas dia1 = reporte.getFilas().get(0);
        assertThat(dia1.getFechaFormateada()).isEqualTo("ago-01");
        assertThat(dia1.getVentasDiarias()).isEqualByComparingTo("150000.00");
        assertThat(dia1.getAcumuladoVentas()).isEqualByComparingTo("150000.00");
        assertThat(dia1.getSalidas()).isEmpty();
        assertThat(dia1.getControlClases()).isEmpty();
        assertThat(dia1.getNotas()).isEmpty();

        // Día 2 (sin ventas): Venta 0, Acumulado se mantiene en 150.000
        FilaControlVentas dia2 = reporte.getFilas().get(1);
        assertThat(dia2.getFechaFormateada()).isEqualTo("ago-02");
        assertThat(dia2.getVentasDiarias()).isEqualByComparingTo("0.00");
        assertThat(dia2.getAcumuladoVentas()).isEqualByComparingTo("150000.00");

        // Día 3: Venta 200.000, Acumulado sube a 350.000
        FilaControlVentas dia3 = reporte.getFilas().get(2);
        assertThat(dia3.getFechaFormateada()).isEqualTo("ago-03");
        assertThat(dia3.getVentasDiarias()).isEqualByComparingTo("200000.00");
        assertThat(dia3.getAcumuladoVentas()).isEqualByComparingTo("350000.00");

        // Día 31: Venta 50.000, Acumulado final 400.000
        FilaControlVentas dia31 = reporte.getFilas().get(30);
        assertThat(dia31.getFechaFormateada()).isEqualTo("ago-31");
        assertThat(dia31.getVentasDiarias()).isEqualByComparingTo("50000.00");
        assertThat(dia31.getAcumuladoVentas()).isEqualByComparingTo("400000.00");
    }

    @Test
    @DisplayName("Debe lanzar BusinessRuleException si el mes es inválido")
    void debeLanzarExcepcionConMesInvalido() {
        assertThatThrownBy(() -> useCase.generarExcelControlVentas(2026, 13))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("entre 1 y 12");
    }

    @Test
    @DisplayName("Debe delegar la generación binaria del Excel al ReporteExcelGateway")
    void debeDelegarGeneracionDeExcel() {
        byte[] contenidoEsperado = new byte[]{1, 2, 3, 4};
        when(resumenVentaGateway.findByFechaBetween(any(), any())).thenReturn(List.of());
        when(reporteExcelGateway.generarReporteControlVentas(any())).thenReturn(contenidoEsperado);

        byte[] resultado = useCase.generarExcelControlVentas(2026, 8);

        assertThat(resultado).isEqualTo(contenidoEsperado);
        verify(reporteExcelGateway).generarReporteControlVentas(any(ReporteMensualVentas.class));
    }
}
