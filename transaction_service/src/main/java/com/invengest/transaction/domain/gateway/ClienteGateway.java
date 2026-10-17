package com.invengest.transaction.domain.gateway;

import com.invengest.transaction.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteGateway {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(Integer idCliente);
    Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);
    List<Cliente> findAll();
    boolean existsById(Integer idCliente);
}
