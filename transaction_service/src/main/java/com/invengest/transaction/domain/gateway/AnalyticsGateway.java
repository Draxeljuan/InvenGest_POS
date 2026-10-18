package com.invengest.transaction.domain.gateway;

import com.invengest.transaction.domain.model.Venta;

public interface AnalyticsGateway {
    void notificarVenta(Venta venta);
}
