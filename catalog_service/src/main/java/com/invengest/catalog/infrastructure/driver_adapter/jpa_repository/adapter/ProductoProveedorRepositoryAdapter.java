package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.catalog.domain.gateway.ProductoProveedorGateway;
import com.invengest.catalog.domain.model.ProductoProveedor;
import com.invengest.catalog.infrastructure.driver_adapter.entity.ProductoProveedorDataId;
import com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces.ProductoProveedorJpaRepository;
import com.invengest.catalog.infrastructure.mapper.ProductoProveedorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductoProveedorRepositoryAdapter implements ProductoProveedorGateway {

    private final ProductoProveedorJpaRepository productoProveedorJpaRepository;
    private final ProductoProveedorMapper productoProveedorMapper;

    @Override
    public List<ProductoProveedor> findByProductoId(String idProducto) {
        return productoProveedorJpaRepository.findById_IdProducto(idProducto)
                .stream().map(productoProveedorMapper::toDomain).toList();
    }

    @Override
    public List<ProductoProveedor> findByProveedorId(Integer idProveedor) {
        return productoProveedorJpaRepository.findById_IdProveedor(idProveedor)
                .stream().map(productoProveedorMapper::toDomain).toList();
    }

    @Override
    public ProductoProveedor save(ProductoProveedor productoProveedor) {
        var entity = productoProveedorMapper.toEntity(productoProveedor);
        return productoProveedorMapper.toDomain(productoProveedorJpaRepository.save(entity));
    }

    @Override
    public void delete(String idProducto, Integer idProveedor) {
        productoProveedorJpaRepository.deleteById(new ProductoProveedorDataId(idProducto, idProveedor));
    }
}