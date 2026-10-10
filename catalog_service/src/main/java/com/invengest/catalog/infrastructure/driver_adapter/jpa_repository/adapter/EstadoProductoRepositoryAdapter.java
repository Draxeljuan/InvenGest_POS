package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.catalog.domain.gateway.EstadoProductoGateway;
import com.invengest.catalog.domain.model.EstadoProducto;
import com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces.EstadoProductoJpaRepository;
import com.invengest.catalog.infrastructure.mapper.EstadoProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EstadoProductoRepositoryAdapter implements EstadoProductoGateway {

    private final EstadoProductoJpaRepository estadoProductoJpaRepository;
    private final EstadoProductoMapper estadoProductoMapper;

    @Override
    public Optional<EstadoProducto> findById(Integer idEstado) {
        return estadoProductoJpaRepository.findById(idEstado).map(estadoProductoMapper::toDomain);
    }

    @Override
    public Optional<EstadoProducto> findByNombre(String nombre) {
        return estadoProductoJpaRepository.findByNombre(nombre).map(estadoProductoMapper::toDomain);
    }

    @Override
    public List<EstadoProducto> findAll() {
        return estadoProductoJpaRepository.findAll().stream().map(estadoProductoMapper::toDomain).toList();
    }
}