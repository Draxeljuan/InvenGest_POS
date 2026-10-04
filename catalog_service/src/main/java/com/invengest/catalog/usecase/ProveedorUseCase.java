package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.ProveedorGateway;
import com.invengest.catalog.domain.model.Proveedor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProveedorUseCase { // Por crear validaciones de negocio más especificas

    private final ProveedorGateway proveedorGateway;

    public Proveedor crear(Proveedor proveedor) {
        if (proveedor.getNit() != null && proveedorGateway.findByNit(proveedor.getNit()).isPresent()) {
            throw new DuplicateEntityException("Ya existe un proveedor registrado con el NIT: " + proveedor.getNit());
        }
        return proveedorGateway.save(proveedor);
    }

    public Proveedor actualizar(Proveedor proveedor) {
        if (proveedor.getIdProveedor() == null || proveedorGateway.findById(proveedor.getIdProveedor()).isEmpty()) {
            throw new EntityNotFoundException("El proveedor a actualizar no existe.");
        }
        return proveedorGateway.save(proveedor);
    }

    public List<Proveedor> obtenerTodos() {
        return proveedorGateway.findAll();
    }

    public Proveedor obtenerPorId(Integer id) {
        return proveedorGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + id));
    }

    public void eliminarPorId(Integer id) {
        if (proveedorGateway.findById(id).isEmpty()) {
            throw new EntityNotFoundException("El proveedor no existe.");
        }
        if (proveedorGateway.existsProductosByProveedorId(id)) {
            throw new EntityInUseException("No se puede eliminar el proveedor porque está vinculado a productos.");
        }
        proveedorGateway.deleteById(id);
    }
}