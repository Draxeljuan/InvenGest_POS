package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.exception.BusinessRuleException;
import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.domain.model.TopProducto;

import java.time.LocalDate;
import java.util.List;

public class ConsultarTopProductosUseCase {

    private static final int LIMITE_POR_DEFECTO = 10;

    private final TopProductoGateway topProductoGateway;

    public ConsultarTopProductosUseCase(TopProductoGateway topProductoGateway) {
        this.topProductoGateway = topProductoGateway;
    }

    public List<TopProducto> obtenerPorFecha(LocalDate fecha) {
        LocalDate fechaConsulta = fecha != null ? fecha : LocalDate.now();
        return topProductoGateway.findByFecha(fechaConsulta);
    }

    public List<TopProducto> obtenerTopPorRango(LocalDate inicio, LocalDate fin, Integer limite) {
        if (inicio == null || fin == null) {
            throw new BusinessRuleException("Las fechas de inicio y fin son obligatorias.");
        }
        if (inicio.isAfter(fin)) {
            throw new BusinessRuleException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        int limit = (limite != null && limite > 0) ? limite : LIMITE_POR_DEFECTO;
        return topProductoGateway.findTopNByFechaBetween(inicio, fin, limit);
    }
}
