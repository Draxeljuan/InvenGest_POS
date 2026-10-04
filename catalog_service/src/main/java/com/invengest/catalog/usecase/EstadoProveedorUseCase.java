package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.gateway.EstadoProveedorGateway;
import com.invengest.catalog.domain.model.EstadoProveedor;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EstadoProveedorUseCase {

    private final EstadoProveedorGateway estadoProveedorGateway;

    public List<EstadoProveedor> obtenerTodos(){
        return estadoProveedorGateway.findAll();
    }

    public EstadoProveedor obtenerPorId(Integer id){
        return estadoProveedorGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estado de Proveedor no encontrado con el id" + id));
    }

}
