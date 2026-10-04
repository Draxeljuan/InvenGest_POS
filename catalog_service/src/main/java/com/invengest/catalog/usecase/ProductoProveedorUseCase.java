package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.gateway.ProductoProveedorGateway;
import com.invengest.catalog.domain.model.ProductoProveedor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductoProveedorUseCase {

    private final ProductoProveedorGateway productoProveedorGateway;

    public ProductoProveedor asociarOActualizar(ProductoProveedor productoProveedor) {
        return productoProveedorGateway.save(productoProveedor);
    }

    public List<ProductoProveedor> obtenerPorProducto(String idProducto) {
        return productoProveedorGateway.findByProductoId(idProducto);
    }

    public List<ProductoProveedor> obtenerPorProveedor(Integer idProveedor) {
        return productoProveedorGateway.findByProveedorId(idProveedor);
    }

    public void eliminarAsociacion(String idProducto, Integer idProveedor) {
        productoProveedorGateway.delete(idProducto, idProveedor);
    }
}