package com.invengest.transaction.domain.gateway;

import com.invengest.transaction.domain.model.TipoMovimiento;

import java.util.List;
import java.util.Optional;

public interface TipoMovimientoGateway {
    Optional<TipoMovimiento> findById(Integer idMovimiento);
    Optional<TipoMovimiento> findByNombre(String nombre);
    List<TipoMovimiento> findAll();
}
