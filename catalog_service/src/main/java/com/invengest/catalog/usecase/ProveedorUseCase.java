package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.EstadoProveedorGateway;
import com.invengest.catalog.domain.gateway.ProveedorGateway;
import com.invengest.catalog.domain.model.EstadoProveedor;
import com.invengest.catalog.domain.model.Proveedor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProveedorUseCase {

    private final ProveedorGateway proveedorGateway;
    private final EstadoProveedorGateway estadoProveedorGateway;

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

    public void eliminacionLogica(Integer id) {

        Proveedor proveedorADescontinuar = proveedorGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El proveedor no existe."));

        if (proveedorGateway.existsProductosByProveedorId(id)) {
            throw new EntityInUseException("No se puede dar de baja el proveedor porque tiene productos asociados.");
        }

        EstadoProveedor estadoInactivo = estadoProveedorGateway.findById(2)
                .orElseThrow(() -> new IllegalStateException("El estado 'Inactivo' (ID 2) no está configurado en el sistema de proveedores."));

        proveedorADescontinuar.setEstado(estadoInactivo);

        proveedorGateway.save(proveedorADescontinuar);
    }
}