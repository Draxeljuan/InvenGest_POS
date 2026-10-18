package com.invengest.analytics.domain.gateway;

import com.invengest.analytics.domain.model.TopProducto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TopProductoGateway {
    List<TopProducto> findByFecha(LocalDate fecha);
    List<TopProducto> findByFechaBetween(LocalDate inicio, LocalDate fin);
    List<TopProducto> findTopNByFechaBetween(LocalDate inicio, LocalDate fin, int limit);
    Optional<TopProducto> findByIdProductoAndFecha(String idProducto, LocalDate fecha);
    TopProducto save(TopProducto topProducto);
}
