package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.transaction.domain.gateway.MetodoPagoGateway;
import com.invengest.transaction.domain.model.MetodoPago;
import com.invengest.transaction.infrastructure.driver_adapter.entity.MetodoPagoData;
import com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces.MetodoPagoJpaRepository;
import com.invengest.transaction.infrastructure.mapper.MetodoPagoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MetodoPagoRepositoryAdapter implements MetodoPagoGateway {

    private final MetodoPagoJpaRepository repository;
    private final MetodoPagoMapper mapper;

    @Override
    public Optional<MetodoPago> findById(Integer idMetodoPago) {
        return repository.findById(idMetodoPago).map(mapper::toDomain);
    }

    @Override
    public List<MetodoPago> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Integer idMetodoPago) {
        return repository.existsById(idMetodoPago);
    }
}
