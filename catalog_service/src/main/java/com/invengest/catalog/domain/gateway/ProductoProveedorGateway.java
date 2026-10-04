package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.ProductoProveedor;
import java.util.List;

public interface ProductoProveedorGateway {
    List<ProductoProveedor> findByProductoId(String idProducto);
    List<ProductoProveedor> findByProveedorId(Integer idProveedor);
    ProductoProveedor save(ProductoProveedor productoProveedor);
    void delete(String idProducto, Integer idProveedor);
}