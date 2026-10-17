package com.invengest.transaction.domain.gateway;

import com.invengest.transaction.domain.model.MovimientoInventario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoInventarioGateway {
    MovimientoInventario save(MovimientoInventario movimiento);
    Optional<MovimientoInventario> findById(Integer idMovimientoInventario);
    List<MovimientoInventario> findAll();
    List<MovimientoInventario> findByIdProducto(String idProducto);
    List<MovimientoInventario> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
