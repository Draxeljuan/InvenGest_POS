package com.invengest.analytics.domain.gateway;

import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.domain.model.TopProducto;

import java.time.LocalDate;
import java.util.List;

public interface ReportePdfGateway {
    byte[] generarReporteVentasPdf(LocalDate inicio, LocalDate fin, List<ResumenVentaDiaria> ventas, List<TopProducto> topProductos);
    byte[] generarReporteAlertasStockPdf(List<AlertaStock> alertas);
}
