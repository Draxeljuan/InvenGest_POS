package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorGateway {
    Optional<Proveedor> findById(Integer idProveedor);
    Optional<Proveedor> findByNit(String nit);
    List<Proveedor> findAll();
    Proveedor save(Proveedor proveedor);
    void deleteById(Integer idProveedor);
    boolean existsProductosByProveedorId(Integer idProveedor);
}