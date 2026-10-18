package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.Producto;

public interface AnalyticsGateway {
    void notificarEstadoStock(Producto producto);
}
