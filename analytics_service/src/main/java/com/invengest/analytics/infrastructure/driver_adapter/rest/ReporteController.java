package com.invengest.analytics.infrastructure.driver_adapter.rest;

import com.invengest.analytics.usecase.GenerarReporteControlVentasUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics/reports")
@RequiredArgsConstructor
public class ReporteController {

    private static final MediaType MEDIA_TYPE_EXCEL = MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final GenerarReporteControlVentasUseCase generarReporteUseCase;

    @GetMapping("/ventas/excel")
    public ResponseEntity<byte[]> descargarReporteControlVentasExcel(
            @RequestParam("anio") int anio,
            @RequestParam("mes") int mes) {
        byte[] archivoExcel = generarReporteUseCase.generarExcelControlVentas(anio, mes);
        String filename = String.format("control_ventas_%d_%02d.xlsx", anio, mes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MEDIA_TYPE_EXCEL);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(archivoExcel);
    }

    @GetMapping("/ventas/pdf")
    public ResponseEntity<byte[]> descargarReporteVentasPdf(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        byte[] archivoPdf = generarReporteUseCase.generarPdfVentasPorRango(inicio, fin);
        String filename = String.format("reporte_ventas_%s_%s.pdf", inicio, fin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(archivoPdf);
    }

    @GetMapping("/alertas-stock/pdf")
    public ResponseEntity<byte[]> descargarReporteAlertasStockPdf() {
        byte[] archivoPdf = generarReporteUseCase.generarPdfAlertasStock();
        String filename = String.format("reporte_alertas_stock_%s.pdf", LocalDate.now());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(archivoPdf);
    }
}
