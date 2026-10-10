package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.EstadoProducto;

import java.util.List;
import java.util.Optional;

public interface EstadoProductoGateway {

    Optional<EstadoProducto> findById(Integer idEstadoProducto);
    List<EstadoProducto> findAll();
    Optional<EstadoProducto> findByNombre(String nombre);
}
