package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.exception.BusinessRuleException;
import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.gateway.ReporteExcelGateway;
import com.invengest.analytics.domain.gateway.ReportePdfGateway;
import com.invengest.analytics.domain.gateway.ResumenVentaGateway;
import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.FilaControlVentas;
import com.invengest.analytics.domain.model.ReporteMensualVentas;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.domain.model.TopProducto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerarReporteControlVentasUseCase {

    private static final Locale LOCALE_ES = Locale.forLanguageTag("es-CO");
    private static final String NOMBRE_NEGOCIO = "\"VARIEDADES MARELIZA\"";

    private final ResumenVentaGateway resumenVentaGateway;
    private final TopProductoGateway topProductoGateway;
    private final AlertaStockGateway alertaStockGateway;
    private final ReporteExcelGateway reporteExcelGateway;
    private final ReportePdfGateway reportePdfGateway;

    public GenerarReporteControlVentasUseCase(
            ResumenVentaGateway resumenVentaGateway,
            TopProductoGateway topProductoGateway,
            AlertaStockGateway alertaStockGateway,
            ReporteExcelGateway reporteExcelGateway,
            ReportePdfGateway reportePdfGateway) {
        this.resumenVentaGateway = resumenVentaGateway;
        this.topProductoGateway = topProductoGateway;
        this.alertaStockGateway = alertaStockGateway;
        this.reporteExcelGateway = reporteExcelGateway;
        this.reportePdfGateway = reportePdfGateway;
    }

    public ReporteMensualVentas construirDatosReporteMensual(int anio, int mes) {
        validarAnioYMes(anio, mes);

        YearMonth yearMonth = YearMonth.of(anio, mes);
        LocalDate inicioMes = yearMonth.atDay(1);
        LocalDate finMes = yearMonth.atEndOfMonth();

        List<ResumenVentaDiaria> ventasRegistradas = resumenVentaGateway.findByFechaBetween(inicioMes, finMes);

        Map<LocalDate, BigDecimal> ventasPorDia = ventasRegistradas.stream()
                .collect(Collectors.toMap(
                        ResumenVentaDiaria::getFecha,
                        v -> v.getTotalVentas() != null ? v.getTotalVentas() : BigDecimal.ZERO,
                        BigDecimal::add
                ));

        List<FilaControlVentas> filas = new ArrayList<>(yearMonth.lengthOfMonth());
        BigDecimal acumuladoVentas = BigDecimal.ZERO;

        for (int dia = 1; dia <= yearMonth.lengthOfMonth(); dia++) {
            LocalDate fechaDia = yearMonth.atDay(dia);
            BigDecimal ventasDiarias = ventasPorDia.getOrDefault(fechaDia, BigDecimal.ZERO);
            acumuladoVentas = acumuladoVentas.add(ventasDiarias);

            filas.add(FilaControlVentas.builder()
                    .fecha(fechaDia)
                    .fechaFormateada(formatearFechaCorta(fechaDia))
                    .ventasDiarias(ventasDiarias)
                    .acumuladoVentas(acumuladoVentas)
                    .salidas("")
                    .controlClases("")
                    .notas("")
                    .build());
        }

        String nombreMes = yearMonth.getMonth()
                .getDisplayName(TextStyle.FULL, LOCALE_ES)
                .toUpperCase(LOCALE_ES);

        String tituloCabecera = String.format("%s %s %s", NOMBRE_NEGOCIO, nombreMes, formatearAnio(anio));

        return ReporteMensualVentas.builder()
                .anio(anio)
                .mes(mes)
                .nombreMes(nombreMes)
                .tituloCabecera(tituloCabecera)
                .filas(filas)
                .totalMes(acumuladoVentas)
                .build();
    }

    public byte[] generarExcelControlVentas(int anio, int mes) {
        ReporteMensualVentas datosReporte = construirDatosReporteMensual(anio, mes);
        return reporteExcelGateway.generarReporteControlVentas(datosReporte);
    }

    public byte[] generarPdfVentasPorRango(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            throw new BusinessRuleException("Las fechas de inicio y fin son obligatorias.");
        }
        if (inicio.isAfter(fin)) {
            throw new BusinessRuleException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        List<ResumenVentaDiaria> ventas = resumenVentaGateway.findByFechaBetween(inicio, fin);
        List<TopProducto> topProductos = topProductoGateway.findTopNByFechaBetween(inicio, fin, 10);
        return reportePdfGateway.generarReporteVentasPdf(inicio, fin, ventas, topProductos);
    }

    public byte[] generarPdfAlertasStock() {
        List<AlertaStock> alertas = alertaStockGateway.findAll();
        return reportePdfGateway.generarReporteAlertasStockPdf(alertas);
    }

    private void validarAnioYMes(int anio, int mes) {
        if (mes < 1 || mes > 12) {
            throw new BusinessRuleException("El mes debe estar comprendido entre 1 y 12.");
        }
        if (anio < 2000 || anio > 2100) {
            throw new BusinessRuleException("El año especificado no es válido.");
        }
    }

    private String formatearFechaCorta(LocalDate fecha) {
        String mesCorto = fecha.getMonth()
                .getDisplayName(TextStyle.SHORT, LOCALE_ES)
                .toLowerCase(LOCALE_ES)
                .replace(".", "");
        if (mesCorto.length() > 3) {
            mesCorto = mesCorto.substring(0, 3);
        }
        return String.format("%s-%02d", mesCorto, fecha.getDayOfMonth());
    }

    private String formatearAnio(int anio) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(LOCALE_ES);
        symbols.setGroupingSeparator('.');
        DecimalFormat format = new DecimalFormat("#,###", symbols);
        return format.format(anio);
    }
}
