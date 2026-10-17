package com.invengest.transaction.domain.gateway;

import com.invengest.transaction.domain.model.MetodoPago;

import java.util.List;
import java.util.Optional;

public interface MetodoPagoGateway {
    Optional<MetodoPago> findById(Integer idMetodoPago);
    List<MetodoPago> findAll();
    boolean existsById(Integer idMetodoPago);
}
