package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.gateway.EstadoProveedorGateway;
import com.invengest.catalog.domain.model.EstadoProveedor;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
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

    public EstadoProveedor obtenerPorNombre(String name){
        return estadoProveedorGateway.findByNombre(name)
                .orElseThrow(() -> new EntityNotFoundException("Estado de Proveedor no encontrado con el nombre" + name));
    }

}
