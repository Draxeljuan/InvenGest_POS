package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.transaction.domain.gateway.TipoDocumentoGateway;
import com.invengest.transaction.domain.model.TipoDocumento;
import com.invengest.transaction.infrastructure.driver_adapter.entity.TipoDocumentoData;
import com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces.TipoDocumentoJpaRepository;
import com.invengest.transaction.infrastructure.mapper.TipoDocumentoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TipoDocumentoRepositoryAdapter implements TipoDocumentoGateway {

    private final TipoDocumentoJpaRepository repository;
    private final TipoDocumentoMapper mapper;

    @Override
    public Optional<TipoDocumento> findById(Integer idTipoDocumento) {
        return repository.findById(idTipoDocumento).map(mapper::toDomain);
    }

    @Override
    public List<TipoDocumento> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
