package com.invengest.analytics.domain.gateway;

import com.invengest.analytics.domain.model.AlertaStock;

import java.util.List;
import java.util.Optional;

public interface AlertaStockGateway {
    List<AlertaStock> findAll();
    List<AlertaStock> findByEstado(String estado);
    List<AlertaStock> findRecentAlerts(int limit);
    Optional<AlertaStock> findByIdProducto(String idProducto);
    AlertaStock save(AlertaStock alertaStock);
    void deleteByIdProducto(String idProducto);
}
