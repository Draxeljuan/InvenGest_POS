package com.invengest.analytics.infrastructure.driver_adapter.report.pdf;

import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.domain.model.TopProducto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PdfReportAdapterTest {

    private final PdfReportAdapter adapter = new PdfReportAdapter();

    @Test
    @DisplayName("Debe generar archivo PDF válido de ventas y top productos")
    void debeGenerarPdfVentasValido() {
        LocalDate inicio = LocalDate.of(2026, 8, 1);
        LocalDate fin = LocalDate.of(2026, 8, 31);

        List<ResumenVentaDiaria> ventas = List.of(
                ResumenVentaDiaria.builder()
                        .fecha(inicio)
                        .totalVentas(new BigDecimal("100000.00"))
                        .cantidadOperaciones(8)
                        .build()
        );

        List<TopProducto> top = List.of(
                TopProducto.builder()
                        .idTop(1)
                        .idProducto("PROD-01")
                        .nombreProducto("Resma Papel Carta")
                        .cantidadVendida(15)
                        .fecha(inicio)
                        .build()
        );

        byte[] pdfBytes = adapter.generarReporteVentasPdf(inicio, fin, ventas, top);

        assertThat(pdfBytes).isNotEmpty();
        String cabeceraPdf = new String(pdfBytes, 0, 4, StandardCharsets.US_ASCII);
        assertThat(cabeceraPdf).isEqualTo("%PDF");
    }

    @Test
    @DisplayName("Debe generar archivo PDF válido de alertas de stock")
    void debeGenerarPdfAlertasValido() {
        List<AlertaStock> alertas = List.of(
                AlertaStock.builder()
                        .idAlerta(1)
                        .idProducto("PROD-02")
                        .nombreProducto("Tijeras")
                        .stockActual((short) 2)
                        .estado("Bajo Stock")
                        .fechaAlerta(LocalDateTime.now())
                        .build()
        );

        byte[] pdfBytes = adapter.generarReporteAlertasStockPdf(alertas);

        assertThat(pdfBytes).isNotEmpty();
        String cabeceraPdf = new String(pdfBytes, 0, 4, StandardCharsets.US_ASCII);
        assertThat(cabeceraPdf).isEqualTo("%PDF");
    }
}
