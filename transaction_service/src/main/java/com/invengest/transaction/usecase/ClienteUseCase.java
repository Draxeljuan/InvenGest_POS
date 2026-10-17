package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.BusinessRuleException;
import com.invengest.transaction.domain.exception.DuplicateEntityException;
import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.gateway.ClienteGateway;
import com.invengest.transaction.domain.gateway.TipoDocumentoGateway;
import com.invengest.transaction.domain.model.Cliente;
import com.invengest.transaction.domain.model.TipoDocumento;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ClienteUseCase {

    private final ClienteGateway clienteGateway;
    private final TipoDocumentoGateway tipoDocumentoGateway;

    public Cliente registrar(Cliente cliente) {
        if (cliente == null) {
            throw new BusinessRuleException("Los datos del cliente son obligatorios");
        }
        if (cliente.getPrimerNombre() == null || cliente.getPrimerNombre().isBlank()) {
            throw new BusinessRuleException("El primer nombre es obligatorio");
        }
        if (cliente.getPrimerApellido() == null || cliente.getPrimerApellido().isBlank()) {
            throw new BusinessRuleException("El primer apellido es obligatorio");
        }

        if (cliente.getDocumentoIdentidad() != null && !cliente.getDocumentoIdentidad().isBlank()) {
            clienteGateway.findByDocumentoIdentidad(cliente.getDocumentoIdentidad()).ifPresent(c -> {
                throw new DuplicateEntityException("Ya existe un cliente con el documento: " + cliente.getDocumentoIdentidad());
            });
        }

        if (cliente.getTipoDocumento() != null && cliente.getTipoDocumento().getIdTipoDocumento() != null) {
            TipoDocumento tipoDoc = tipoDocumentoGateway.findById(cliente.getTipoDocumento().getIdTipoDocumento())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de documento no encontrado con ID: " + cliente.getTipoDocumento().getIdTipoDocumento()));
            cliente.setTipoDocumento(tipoDoc);
        }

        return clienteGateway.save(cliente);
    }

    public Cliente obtenerPorId(Integer idCliente) {
        return clienteGateway.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + idCliente));
    }

    public Cliente obtenerPorDocumento(String documento) {
        return clienteGateway.findByDocumentoIdentidad(documento)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con documento: " + documento));
    }

    public List<Cliente> listarTodos() {
        return clienteGateway.findAll();
    }
}
