package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.catalog.domain.gateway.ProductoGateway;
import com.invengest.catalog.domain.model.Producto;
import com.invengest.catalog.infrastructure.driver_adapter.entity.ProductoData;
import com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces.ProductoJpaRepository;
import com.invengest.catalog.infrastructure.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductoRepositoryAdapter implements ProductoGateway {

    private final ProductoJpaRepository productoJpaRepository;
    private final ProductoMapper productoMapper;

    @Override
    public Optional<Producto> findById(String idProducto) {
        return productoJpaRepository.findById(idProducto).map(productoMapper::toDomain);
    }

    @Override
    public List<Producto> findAll() {
        return productoJpaRepository.findAll().stream().map(productoMapper::toDomain).toList();
    }

    @Override
    public List<Producto> findBySearchCriteria(String query) {
        return productoJpaRepository.searchByCriteria(query).stream().map(productoMapper::toDomain).toList();
    }

    @Override
    public List<Producto> findLowStockProducts() {
        return productoJpaRepository.findLowStockProducts().stream().map(productoMapper::toDomain).toList();
    }

    @Override
    public Producto save(Producto producto) {
        ProductoData entity = productoMapper.toEntity(producto);
        return productoMapper.toDomain(productoJpaRepository.save(entity));
    }

    @Override
    public boolean existsById(String idProducto) {
        return productoJpaRepository.existsById(idProducto);
    }
}