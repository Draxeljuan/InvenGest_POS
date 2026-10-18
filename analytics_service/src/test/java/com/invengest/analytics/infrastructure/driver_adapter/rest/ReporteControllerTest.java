package com.invengest.analytics.infrastructure.driver_adapter.rest;

import com.invengest.analytics.domain.exception.BusinessRuleException;
import com.invengest.analytics.usecase.GenerarReporteControlVentasUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenerarReporteControlVentasUseCase generarReporteUseCase;

    @Test
    @DisplayName("GET /api/analytics/reports/ventas/excel - Descarga reporte Excel con headers y MediaType correctos")
    void descargarExcel_DebeRetornar200YArchivoBinario() throws Exception {
        byte[] dummyBytes = new byte[]{80, 75, 3, 4};
        when(generarReporteUseCase.generarExcelControlVentas(2026, 8)).thenReturn(dummyBytes);

        mockMvc.perform(get("/api/analytics/reports/ventas/excel")
                        .param("anio", "2026")
                        .param("mes", "8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"control_ventas_2026_08.xlsx\""))
                .andExpect(content().bytes(dummyBytes));
    }

    @Test
    @DisplayName("GET /api/analytics/reports/ventas/excel - Retorna 400 cuando el mes es inválido")
    void descargarExcel_MesInvalido_DebeRetornar400() throws Exception {
        when(generarReporteUseCase.generarExcelControlVentas(2026, 15))
                .thenThrow(new BusinessRuleException("El mes debe estar comprendido entre 1 y 12."));

        mockMvc.perform(get("/api/analytics/reports/ventas/excel")
                        .param("anio", "2026")
                        .param("mes", "15"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El mes debe estar comprendido entre 1 y 12."));
    }

    @Test
    @DisplayName("GET /api/analytics/reports/ventas/pdf - Descarga reporte PDF de ventas")
    void descargarPdfVentas_DebeRetornar200() throws Exception {
        byte[] dummyPdf = new byte[]{37, 80, 68, 70};
        LocalDate inicio = LocalDate.of(2026, 8, 1);
        LocalDate fin = LocalDate.of(2026, 8, 31);
        when(generarReporteUseCase.generarPdfVentasPorRango(inicio, fin)).thenReturn(dummyPdf);

        mockMvc.perform(get("/api/analytics/reports/ventas/pdf")
                        .param("inicio", "2026-08-01")
                        .param("fin", "2026-08-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(dummyPdf));
    }
}
