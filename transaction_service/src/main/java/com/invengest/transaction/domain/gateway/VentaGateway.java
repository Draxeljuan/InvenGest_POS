package com.invengest.transaction.domain.gateway;

import com.invengest.transaction.domain.model.Venta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaGateway {
    Venta save(Venta venta);
    Optional<Venta> findById(Integer idVenta);
    List<Venta> findAll();
    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Venta> findByIdUsuario(Integer idUsuario);
}
