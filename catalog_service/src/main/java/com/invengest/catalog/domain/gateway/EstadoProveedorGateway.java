package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.EstadoProveedor;

import java.util.List;
import java.util.Optional;

public interface EstadoProveedorGateway {

    Optional<EstadoProveedor> findById(Integer idEstadoProveedor);
    List<EstadoProveedor> findAll();
}
