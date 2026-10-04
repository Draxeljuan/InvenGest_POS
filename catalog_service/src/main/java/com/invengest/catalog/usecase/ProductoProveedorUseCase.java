package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.gateway.ProductoProveedorRepository;
import com.invengest.catalog.domain.model.ProductoProveedor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductoProveedorUseCase {

    private final ProductoProveedorRepository productoProveedorRepository;

    public ProductoProveedor asociarOActualizar(ProductoProveedor productoProveedor) {
        return productoProveedorRepository.save(productoProveedor);
    }

    public List<ProductoProveedor> obtenerPorProducto(String idProducto) {
        return productoProveedorRepository.findByProductoId(idProducto);
    }

    public List<ProductoProveedor> obtenerPorProveedor(Integer idProveedor) {
        return productoProveedorRepository.findByProveedorId(idProveedor);
    }

    public void eliminarAsociacion(String idProducto, Integer idProveedor) {
        productoProveedorRepository.delete(idProducto, idProveedor);
    }
}