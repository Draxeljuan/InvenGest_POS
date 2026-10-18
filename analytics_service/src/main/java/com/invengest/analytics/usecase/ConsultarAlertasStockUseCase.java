package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.exception.BusinessRuleException;
import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.model.AlertaStock;

import java.util.List;
import java.util.Set;

public class ConsultarAlertasStockUseCase {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("Agotado", "Bajo Stock");

    private final AlertaStockGateway alertaStockGateway;

    public ConsultarAlertasStockUseCase(AlertaStockGateway alertaStockGateway) {
        this.alertaStockGateway = alertaStockGateway;
    }

    public List<AlertaStock> obtenerTodas() {
        return alertaStockGateway.findAll();
    }

    public List<AlertaStock> obtenerPorEstado(String estado) {
        if (estado == null || !ESTADOS_VALIDOS.contains(estado)) {
            throw new BusinessRuleException("El estado de alerta debe ser 'Agotado' o 'Bajo Stock'.");
        }
        return alertaStockGateway.findByEstado(estado);
    }

    public List<AlertaStock> obtenerRecientes(Integer limite) {
        int limit = (limite != null && limite > 0) ? limite : 10;
        return alertaStockGateway.findRecentAlerts(limit);
    }
}
