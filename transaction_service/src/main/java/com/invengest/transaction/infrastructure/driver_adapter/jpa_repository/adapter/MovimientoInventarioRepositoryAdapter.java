package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.transaction.domain.gateway.MovimientoInventarioGateway;
import com.invengest.transaction.domain.model.MovimientoInventario;
import com.invengest.transaction.infrastructure.driver_adapter.entity.MovimientoInventarioData;
import com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces.MovimientoInventarioJpaRepository;
import com.invengest.transaction.infrastructure.mapper.MovimientoInventarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovimientoInventarioRepositoryAdapter implements MovimientoInventarioGateway {

    private final MovimientoInventarioJpaRepository repository;
    private final MovimientoInventarioMapper mapper;

    @Override
    public MovimientoInventario save(MovimientoInventario movimiento) {
        MovimientoInventarioData entity = mapper.toEntity(movimiento);
        MovimientoInventarioData saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<MovimientoInventario> findById(Integer idMovimientoInventario) {
        return repository.findById(idMovimientoInventario).map(mapper::toDomain);
    }

    @Override
    public List<MovimientoInventario> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<MovimientoInventario> findByIdProducto(String idProducto) {
        return repository.findByIdProductoOrderByFechaMovimientoDesc(idProducto).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<MovimientoInventario> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return repository.findByFechaMovimientoBetweenOrderByFechaMovimientoDesc(inicio, fin).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
