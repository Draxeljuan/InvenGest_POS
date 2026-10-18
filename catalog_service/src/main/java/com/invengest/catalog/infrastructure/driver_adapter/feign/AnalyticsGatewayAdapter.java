package com.invengest.catalog.infrastructure.driver_adapter.feign;

import com.invengest.catalog.domain.gateway.AnalyticsGateway;
import com.invengest.catalog.domain.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsGatewayAdapter implements AnalyticsGateway {

    private final AnalyticsFeignClient analyticsFeignClient;

    @Override
    public void notificarEstadoStock(Producto producto) {
        if (producto == null) {
            return;
        }

        Short stockActual = producto.getStock() != null
                ? producto.getStock().shortValue()
                : 0;

        SincronizarAlertaStockRequestDto request = new SincronizarAlertaStockRequestDto(
                producto.getIdProducto(),
                producto.getNombre(),
                stockActual
        );

        analyticsFeignClient.sincronizarAlertaStock(request);
    }
}
