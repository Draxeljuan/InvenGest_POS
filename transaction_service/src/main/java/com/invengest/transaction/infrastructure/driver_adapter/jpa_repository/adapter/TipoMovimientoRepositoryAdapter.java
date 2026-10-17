package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.transaction.domain.gateway.TipoMovimientoGateway;
import com.invengest.transaction.domain.model.TipoMovimiento;
import com.invengest.transaction.infrastructure.driver_adapter.entity.TipoMovimientoData;
import com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces.TipoMovimientoJpaRepository;
import com.invengest.transaction.infrastructure.mapper.TipoMovimientoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TipoMovimientoRepositoryAdapter implements TipoMovimientoGateway {

    private final TipoMovimientoJpaRepository repository;
    private final TipoMovimientoMapper mapper;

    @Override
    public Optional<TipoMovimiento> findById(Integer idMovimiento) {
        return repository.findById(idMovimiento).map(mapper::toDomain);
    }

    @Override
    public Optional<TipoMovimiento> findByNombre(String nombre) {
        return repository.findByNombre(nombre).map(mapper::toDomain);
    }

    @Override
    public List<TipoMovimiento> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
