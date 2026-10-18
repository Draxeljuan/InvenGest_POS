package com.invengest.analytics.infrastructure.driver_adapter.report.excel;

import com.invengest.analytics.domain.exception.ReportGenerationException;
import com.invengest.analytics.domain.gateway.ReporteExcelGateway;
import com.invengest.analytics.domain.model.FilaControlVentas;
import com.invengest.analytics.domain.model.ReporteMensualVentas;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Component
public class ExcelControlVentasAdapter implements ReporteExcelGateway {

    private static final String[] COLUMNAS_CABECERA = {
            "FECHA",
            "VENTAS DIARIAS",
            "ACUMULADO VENTAS",
            "SALIDAS",
            "CONTROL CLASES",
            "NOTAS"
    };

    @Override
    public byte[] generarReporteControlVentas(ReporteMensualVentas reporte) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            String nombreHoja = String.format("%s %d", reporte.getNombreMes(), reporte.getAnio());
            Sheet sheet = workbook.createSheet(nombreHoja);

            CellStyle titleStyle = crearEstiloTitulo(workbook);
            CellStyle headerStyle = crearEstiloCabecera(workbook);
            CellStyle dateCellStyle = crearEstiloFecha(workbook);
            CellStyle currencyStyle = crearEstiloMoneda(workbook, false);
            CellStyle blankStyle = crearEstiloCeldaEnBlanco(workbook);
            CellStyle totalLabelStyle = crearEstiloEtiquetaTotal(workbook);
            CellStyle totalCurrencyStyle = crearEstiloMoneda(workbook, true);

            // Fila 0: Título principal ("VARIEDADES MARELIZA" MES AÑO)
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(26);
            for (int col = 0; col < COLUMNAS_CABECERA.length; col++) {
                Cell cell = titleRow.createCell(col);
                cell.setCellStyle(titleStyle);
                if (col == 0) {
                    cell.setCellValue(reporte.getTituloCabecera());
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, COLUMNAS_CABECERA.length - 1));

            // Fila 1: Cabeceras de las 6 columnas del RF05
            Row headerRow = sheet.createRow(1);
            headerRow.setHeightInPoints(22);
            for (int col = 0; col < COLUMNAS_CABECERA.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNAS_CABECERA[col]);
                cell.setCellStyle(headerStyle);
            }

            // Filas 2..N: Datos diarios del mes
            int rowIdx = 2;
            List<FilaControlVentas> filas = reporte.getFilas();
            if (filas != null) {
                for (FilaControlVentas fila : filas) {
                    Row row = sheet.createRow(rowIdx++);

                    Cell cellFecha = row.createCell(0);
                    cellFecha.setCellValue(fila.getFechaFormateada());
                    cellFecha.setCellStyle(dateCellStyle);

                    Cell cellVentasDiarias = row.createCell(1);
                    BigDecimal ventasDiarias = fila.getVentasDiarias() != null ? fila.getVentasDiarias() : BigDecimal.ZERO;
                    cellVentasDiarias.setCellValue(ventasDiarias.doubleValue());
                    cellVentasDiarias.setCellStyle(currencyStyle);

                    Cell cellAcumulado = row.createCell(2);
                    BigDecimal acumulado = fila.getAcumuladoVentas() != null ? fila.getAcumuladoVentas() : BigDecimal.ZERO;
                    cellAcumulado.setCellValue(acumulado.doubleValue());
                    cellAcumulado.setCellStyle(currencyStyle);

                    // Columnas 4, 5 y 6 estrictamente en blanco para edición manual
                    Cell cellSalidas = row.createCell(3);
                    cellSalidas.setCellValue("");
                    cellSalidas.setCellStyle(blankStyle);

                    Cell cellControlClases = row.createCell(4);
                    cellControlClases.setCellValue("");
                    cellControlClases.setCellStyle(blankStyle);

                    Cell cellNotas = row.createCell(5);
                    cellNotas.setCellValue("");
                    cellNotas.setCellStyle(blankStyle);
                }
            }

            // Fila final: TOTAL MES
            Row totalRow = sheet.createRow(rowIdx);
            totalRow.setHeightInPoints(20);

            Cell cellTotalLabel = totalRow.createCell(0);
            cellTotalLabel.setCellValue("TOTAL MES");
            cellTotalLabel.setCellStyle(totalLabelStyle);

            BigDecimal totalMes = reporte.getTotalMes() != null ? reporte.getTotalMes() : BigDecimal.ZERO;

            Cell cellTotalVentas = totalRow.createCell(1);
            cellTotalVentas.setCellValue(totalMes.doubleValue());
            cellTotalVentas.setCellStyle(totalCurrencyStyle);

            Cell cellTotalAcumulado = totalRow.createCell(2);
            cellTotalAcumulado.setCellValue(totalMes.doubleValue());
            cellTotalAcumulado.setCellStyle(totalCurrencyStyle);

            for (int col = 3; col < COLUMNAS_CABECERA.length; col++) {
                Cell blankTotalCell = totalRow.createCell(col);
                blankTotalCell.setCellValue("");
                blankTotalCell.setCellStyle(totalLabelStyle);
            }

            // Ajustar ancho de columnas
            sheet.setColumnWidth(0, 14 * 256);
            sheet.setColumnWidth(1, 20 * 256);
            sheet.setColumnWidth(2, 22 * 256);
            sheet.setColumnWidth(3, 18 * 256);
            sheet.setColumnWidth(4, 22 * 256);
            sheet.setColumnWidth(5, 30 * 256);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar el reporte de Excel de Control de Ventas", e);
        }
    }

    private CellStyle crearEstiloTitulo(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        aplicarBordes(style);
        return style;
    }

    private CellStyle crearEstiloCabecera(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        aplicarBordes(style);
        return style;
    }

    private CellStyle crearEstiloFecha(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        aplicarBordes(style);
        return style;
    }

    private CellStyle crearEstiloMoneda(Workbook workbook, boolean enNegrita) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        if (enNegrita) {
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        aplicarBordes(style);
        return style;
    }

    private CellStyle crearEstiloCeldaEnBlanco(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        aplicarBordes(style);
        return style;
    }

    private CellStyle crearEstiloEtiquetaTotal(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        aplicarBordes(style);
        return style;
    }

    private void aplicarBordes(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
