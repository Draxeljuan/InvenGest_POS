package com.invengest.transaction.infrastructure.configuration;

import com.invengest.transaction.domain.gateway.*;
import com.invengest.transaction.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarVentaUseCase registrarVentaUseCase(
            VentaGateway ventaGateway,
            CatalogGateway catalogGateway,
            MovimientoInventarioGateway movimientoInventarioGateway,
            ClienteGateway clienteGateway,
            MetodoPagoGateway metodoPagoGateway,
            TipoMovimientoGateway tipoMovimientoGateway,
            AnalyticsGateway analyticsGateway) {
        return new RegistrarVentaUseCase(
                ventaGateway,
                catalogGateway,
                movimientoInventarioGateway,
                clienteGateway,
                metodoPagoGateway,
                tipoMovimientoGateway,
                analyticsGateway
        );
    }

    @Bean
    public ConsultarVentaUseCase consultarVentaUseCase(VentaGateway ventaGateway) {
        return new ConsultarVentaUseCase(ventaGateway);
    }

    @Bean
    public MovimientoInventarioUseCase movimientoInventarioUseCase(
            MovimientoInventarioGateway movimientoInventarioGateway,
            TipoMovimientoGateway tipoMovimientoGateway) {
        return new MovimientoInventarioUseCase(movimientoInventarioGateway, tipoMovimientoGateway);
    }

    @Bean
    public ClienteUseCase clienteUseCase(
            ClienteGateway clienteGateway,
            TipoDocumentoGateway tipoDocumentoGateway) {
        return new ClienteUseCase(clienteGateway, tipoDocumentoGateway);
    }

    @Bean
    public MetodoPagoUseCase metodoPagoUseCase(MetodoPagoGateway metodoPagoGateway) {
        return new MetodoPagoUseCase(metodoPagoGateway);
    }
}
