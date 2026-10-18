package com.invengest.transaction.infrastructure.driver_adapter.feign;

import com.invengest.transaction.domain.gateway.AnalyticsGateway;
import com.invengest.transaction.domain.model.TipoItem;
import com.invengest.transaction.domain.model.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalyticsGatewayAdapter implements AnalyticsGateway {

    private final AnalyticsFeignClient analyticsFeignClient;

    @Override
    public void notificarVenta(Venta venta) {
        if (venta == null) {
            return;
        }

        LocalDate fecha = venta.getFecha() != null
                ? venta.getFecha().toLocalDate()
                : LocalDate.now();

        List<ItemVentaAnalyticsDto> items = venta.getDetalles() != null
                ? venta.getDetalles().stream()
                        .filter(d -> d.getTipoItem() == null || d.getTipoItem() == TipoItem.PRODUCTO)
                        .map(d -> new ItemVentaAnalyticsDto(
                                d.getIdItem(),
                                d.getNombreItem(),
                                d.getCantidad()
                        ))
                        .toList()
                : Collections.emptyList();

        SincronizarVentaRequestDto request = new SincronizarVentaRequestDto(
                fecha,
                venta.getTotal(),
                items
        );

        analyticsFeignClient.sincronizarVenta(request);
    }
}
