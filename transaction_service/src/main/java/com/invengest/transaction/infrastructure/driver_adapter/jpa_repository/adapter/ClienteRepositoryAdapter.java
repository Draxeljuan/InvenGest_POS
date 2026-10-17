package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.transaction.domain.gateway.ClienteGateway;
import com.invengest.transaction.domain.model.Cliente;
import com.invengest.transaction.infrastructure.driver_adapter.entity.ClienteData;
import com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces.ClienteJpaRepository;
import com.invengest.transaction.infrastructure.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteGateway {

    private final ClienteJpaRepository clienteJpaRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public Cliente save(Cliente cliente) {
        ClienteData entity = clienteMapper.toEntity(cliente);
        ClienteData saved = clienteJpaRepository.save(entity);
        return clienteMapper.toDomain(saved);
    }

    @Override
    public Optional<Cliente> findById(Integer idCliente) {
        return clienteJpaRepository.findById(idCliente).map(clienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad) {
        return clienteJpaRepository.findByDocumentoIdentidad(documentoIdentidad).map(clienteMapper::toDomain);
    }

    @Override
    public List<Cliente> findAll() {
        return clienteJpaRepository.findAll().stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Integer idCliente) {
        return clienteJpaRepository.existsById(idCliente);
    }
}
