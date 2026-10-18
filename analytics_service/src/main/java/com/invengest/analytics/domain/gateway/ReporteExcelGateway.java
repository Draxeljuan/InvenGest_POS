package com.invengest.analytics.domain.gateway;

import com.invengest.analytics.domain.model.ReporteMensualVentas;

public interface ReporteExcelGateway {
    byte[] generarReporteControlVentas(ReporteMensualVentas reporte);
}
