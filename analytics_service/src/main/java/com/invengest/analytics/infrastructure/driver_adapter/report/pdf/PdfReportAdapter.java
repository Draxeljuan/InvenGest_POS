package com.invengest.analytics.infrastructure.driver_adapter.report.pdf;

import com.invengest.analytics.domain.exception.ReportGenerationException;
import com.invengest.analytics.domain.gateway.ReportePdfGateway;
import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.domain.model.TopProducto;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class PdfReportAdapter implements ReportePdfGateway {

    private static final Locale LOCALE_ES = Locale.forLanguageTag("es-CO");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public byte[] generarReporteVentasPdf(
            LocalDate inicio,
            LocalDate fin,
            List<ResumenVentaDiaria> ventas,
            List<TopProducto> topProductos) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(20, 45, 90));
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);

            Paragraph title = new Paragraph("VARIEDADES MARELIZA - REPORTE DE VENTAS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph period = new Paragraph(
                    String.format("Periodo: %s a %s", inicio.format(DATE_FORMATTER), fin.format(DATE_FORMATTER)),
                    subtitleFont);
            period.setAlignment(Element.ALIGN_CENTER);
            period.setSpacingAfter(18);
            document.add(period);

            // Tabla de Resumen de Ventas Diarias
            Paragraph sectionVentas = new Paragraph("1. Resumen de Ventas Diarias", subtitleFont);
            sectionVentas.setSpacingAfter(8);
            document.add(sectionVentas);

            PdfPTable tableVentas = new PdfPTable(new float[]{3f, 3f, 4f});
            tableVentas.setWidthPercentage(100);
            agregarCabeceraTabla(tableVentas, new String[]{"FECHA", "OPERACIONES", "TOTAL VENTAS"});

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(LOCALE_ES);
            BigDecimal granTotal = BigDecimal.ZERO;
            int totalOperaciones = 0;

            if (ventas != null) {
                for (ResumenVentaDiaria venta : ventas) {
                    BigDecimal total = venta.getTotalVentas() != null ? venta.getTotalVentas() : BigDecimal.ZERO;
                    int ops = venta.getCantidadOperaciones() != null ? venta.getCantidadOperaciones() : 0;
                    granTotal = granTotal.add(total);
                    totalOperaciones += ops;

                    agregarCelda(tableVentas, venta.getFecha().format(DATE_FORMATTER), bodyFont, Element.ALIGN_CENTER);
                    agregarCelda(tableVentas, String.valueOf(ops), bodyFont, Element.ALIGN_CENTER);
                    agregarCelda(tableVentas, currencyFormat.format(total), bodyFont, Element.ALIGN_RIGHT);
                }
            }

            agregarCelda(tableVentas, "TOTAL PERIODO", boldFont, Element.ALIGN_CENTER);
            agregarCelda(tableVentas, String.valueOf(totalOperaciones), boldFont, Element.ALIGN_CENTER);
            agregarCelda(tableVentas, currencyFormat.format(granTotal), boldFont, Element.ALIGN_RIGHT);

            tableVentas.setSpacingAfter(20);
            document.add(tableVentas);

            // Tabla de Productos Más Vendidos
            Paragraph sectionTop = new Paragraph("2. Productos Más Vendidos del Periodo", subtitleFont);
            sectionTop.setSpacingAfter(8);
            document.add(sectionTop);

            PdfPTable tableTop = new PdfPTable(new float[]{2.5f, 5f, 2.5f});
            tableTop.setWidthPercentage(100);
            agregarCabeceraTabla(tableTop, new String[]{"ID PRODUCTO", "NOMBRE PRODUCTO", "CANTIDAD VENDIDA"});

            if (topProductos != null && !topProductos.isEmpty()) {
                for (TopProducto prod : topProductos) {
                    agregarCelda(tableTop, prod.getIdProducto(), bodyFont, Element.ALIGN_CENTER);
                    agregarCelda(tableTop, prod.getNombreProducto(), bodyFont, Element.ALIGN_LEFT);
                    agregarCelda(tableTop, String.valueOf(prod.getCantidadVendida()), bodyFont, Element.ALIGN_CENTER);
                }
            } else {
                PdfPCell emptyCell = new PdfPCell(new Phrase("Sin registros en este rango de fechas", bodyFont));
                emptyCell.setColspan(3);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell.setPadding(6);
                tableTop.addCell(emptyCell);
            }

            document.add(tableTop);
            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new ReportGenerationException("Error al generar el reporte PDF de ventas", e);
        }
    }

    @Override
    public byte[] generarReporteAlertasStockPdf(List<AlertaStock> alertas) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(20, 45, 90));
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

            Paragraph title = new Paragraph("VARIEDADES MARELIZA - REPORTE DE ALERTAS DE STOCK", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(18);
            document.add(title);

            PdfPTable table = new PdfPTable(new float[]{2f, 4f, 1.5f, 2f, 2.5f});
            table.setWidthPercentage(100);
            agregarCabeceraTabla(table, new String[]{"CÓDIGO", "PRODUCTO", "STOCK", "ESTADO", "FECHA ALERTA"});

            if (alertas != null && !alertas.isEmpty()) {
                for (AlertaStock alerta : alertas) {
                    agregarCelda(table, alerta.getIdProducto(), bodyFont, Element.ALIGN_CENTER);
                    agregarCelda(table, alerta.getNombreProducto(), bodyFont, Element.ALIGN_LEFT);
                    agregarCelda(table, String.valueOf(alerta.getStockActual()), bodyFont, Element.ALIGN_CENTER);
                    agregarCelda(table, alerta.getEstado(), bodyFont, Element.ALIGN_CENTER);
                    String fechaStr = alerta.getFechaAlerta() != null
                            ? alerta.getFechaAlerta().format(DATETIME_FORMATTER)
                            : "-";
                    agregarCelda(table, fechaStr, bodyFont, Element.ALIGN_CENTER);
                }
            } else {
                PdfPCell emptyCell = new PdfPCell(new Phrase("No existen alertas activas de stock", bodyFont));
                emptyCell.setColspan(5);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell.setPadding(6);
                table.addCell(emptyCell);
            }

            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new ReportGenerationException("Error al generar el reporte PDF de alertas de stock", e);
        }
    }

    private void agregarCabeceraTabla(PdfPTable table, String[] headers) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Color bgColor = new Color(30, 60, 114);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(6);
            table.addCell(cell);
        }
    }

    private void agregarCelda(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
