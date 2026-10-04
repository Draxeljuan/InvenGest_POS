package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.gateway.EstadoProductoGateway;
import com.invengest.catalog.domain.model.EstadoProducto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EstadoProductoUseCase {
    private final EstadoProductoGateway estadoProductoGateway;

    public List<EstadoProducto> obtenerTodos() {
        return estadoProductoGateway.findAll();
    }

    public EstadoProducto obtenerPorId(Integer id){
        return estadoProductoGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estado de Producto no encontrado con el id" + id));
    }


}
