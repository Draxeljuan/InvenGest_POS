package com.invengest.analytics.infrastructure.driver_adapter.report.excel;

import com.invengest.analytics.domain.model.FilaControlVentas;
import com.invengest.analytics.domain.model.ReporteMensualVentas;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExcelControlVentasAdapterTest {

    private final ExcelControlVentasAdapter adapter = new ExcelControlVentasAdapter();

    @Test
    @DisplayName("Debe generar archivo Excel con las 6 columnas exactas del RF05, las últimas 3 en blanco y fila TOTAL MES")
    void debeGenerarEstructuraExcelSegunRF05() throws IOException {
        List<FilaControlVentas> filas = List.of(
                FilaControlVentas.builder()
                        .fecha(LocalDate.of(2026, 8, 1))
                        .fechaFormateada("ago-01")
                        .ventasDiarias(new BigDecimal("120000.00"))
                        .acumuladoVentas(new BigDecimal("120000.00"))
                        .salidas("")
                        .controlClases("")
                        .notas("")
                        .build(),
                FilaControlVentas.builder()
                        .fecha(LocalDate.of(2026, 8, 2))
                        .fechaFormateada("ago-02")
                        .ventasDiarias(new BigDecimal("80000.00"))
                        .acumuladoVentas(new BigDecimal("200000.00"))
                        .salidas("")
                        .controlClases("")
                        .notas("")
                        .build()
        );

        ReporteMensualVentas reporte = ReporteMensualVentas.builder()
                .anio(2026)
                .mes(8)
                .nombreMes("AGOSTO")
                .tituloCabecera("\"VARIEDADES MARELIZA\" AGOSTO 2.026")
                .filas(filas)
                .totalMes(new BigDecimal("200000.00"))
                .build();

        byte[] excelBytes = adapter.generarReporteControlVentas(reporte);
        assertThat(excelBytes).isNotEmpty();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = workbook.getSheet("AGOSTO 2026");
            assertThat(sheet).isNotNull();

            // Fila 0: Título principal
            Row titleRow = sheet.getRow(0);
            assertThat(titleRow.getCell(0).getStringCellValue())
                    .isEqualTo("\"VARIEDADES MARELIZA\" AGOSTO 2.026");

            // Fila 1: Cabeceras exactas requeridas por RF05
            Row headerRow = sheet.getRow(1);
            assertThat(headerRow.getCell(0).getStringCellValue()).isEqualTo("FECHA");
            assertThat(headerRow.getCell(1).getStringCellValue()).isEqualTo("VENTAS DIARIAS");
            assertThat(headerRow.getCell(2).getStringCellValue()).isEqualTo("ACUMULADO VENTAS");
            assertThat(headerRow.getCell(3).getStringCellValue()).isEqualTo("SALIDAS");
            assertThat(headerRow.getCell(4).getStringCellValue()).isEqualTo("CONTROL CLASES");
            assertThat(headerRow.getCell(5).getStringCellValue()).isEqualTo("NOTAS");

            // Fila 2 (Día 1): Columnas 4, 5 y 6 deben estar en blanco
            Row rowDia1 = sheet.getRow(2);
            assertThat(rowDia1.getCell(0).getStringCellValue()).isEqualTo("ago-01");
            assertThat(rowDia1.getCell(1).getNumericCellValue()).isEqualTo(120000.00);
            assertThat(rowDia1.getCell(2).getNumericCellValue()).isEqualTo(120000.00);
            assertThat(rowDia1.getCell(3).getStringCellValue()).isEmpty();
            assertThat(rowDia1.getCell(4).getStringCellValue()).isEmpty();
            assertThat(rowDia1.getCell(5).getStringCellValue()).isEmpty();

            // Fila 4: TOTAL MES
            Row totalRow = sheet.getRow(4);
            assertThat(totalRow.getCell(0).getStringCellValue()).isEqualTo("TOTAL MES");
            assertThat(totalRow.getCell(1).getNumericCellValue()).isEqualTo(200000.00);
            assertThat(totalRow.getCell(2).getNumericCellValue()).isEqualTo(200000.00);
        }
    }
}
