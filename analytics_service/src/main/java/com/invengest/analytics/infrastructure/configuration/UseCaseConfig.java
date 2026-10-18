package com.invengest.analytics.infrastructure.configuration;

import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.gateway.ReporteExcelGateway;
import com.invengest.analytics.domain.gateway.ReportePdfGateway;
import com.invengest.analytics.domain.gateway.ResumenVentaGateway;
import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.usecase.ConsultarAlertasStockUseCase;
import com.invengest.analytics.usecase.ConsultarDashboardUseCase;
import com.invengest.analytics.usecase.ConsultarTopProductosUseCase;
import com.invengest.analytics.usecase.GenerarReporteControlVentasUseCase;
import com.invengest.analytics.usecase.SincronizarAnaliticaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public GenerarReporteControlVentasUseCase generarReporteControlVentasUseCase(
            ResumenVentaGateway resumenVentaGateway,
            TopProductoGateway topProductoGateway,
            AlertaStockGateway alertaStockGateway,
            ReporteExcelGateway reporteExcelGateway,
            ReportePdfGateway reportePdfGateway) {
        return new GenerarReporteControlVentasUseCase(
                resumenVentaGateway,
                topProductoGateway,
                alertaStockGateway,
                reporteExcelGateway,
                reportePdfGateway
        );
    }

    @Bean
    public ConsultarDashboardUseCase consultarDashboardUseCase(
            ResumenVentaGateway resumenVentaGateway,
            TopProductoGateway topProductoGateway,
            AlertaStockGateway alertaStockGateway) {
        return new ConsultarDashboardUseCase(
                resumenVentaGateway,
                topProductoGateway,
                alertaStockGateway
        );
    }

    @Bean
    public ConsultarTopProductosUseCase consultarTopProductosUseCase(
            TopProductoGateway topProductoGateway) {
        return new ConsultarTopProductosUseCase(topProductoGateway);
    }

    @Bean
    public ConsultarAlertasStockUseCase consultarAlertasStockUseCase(
            AlertaStockGateway alertaStockGateway) {
        return new ConsultarAlertasStockUseCase(alertaStockGateway);
    }

    @Bean
    public SincronizarAnaliticaUseCase sincronizarAnaliticaUseCase(
            ResumenVentaGateway resumenVentaGateway,
            TopProductoGateway topProductoGateway,
            AlertaStockGateway alertaStockGateway) {
        return new SincronizarAnaliticaUseCase(
                resumenVentaGateway,
                topProductoGateway,
                alertaStockGateway
        );
    }
}
