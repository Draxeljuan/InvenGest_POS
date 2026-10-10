package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.catalog.domain.gateway.ProveedorGateway;
import com.invengest.catalog.domain.model.Proveedor;
import com.invengest.catalog.infrastructure.driver_adapter.entity.ProveedorData;
import com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces.ProveedorJpaRepository;
import com.invengest.catalog.infrastructure.mapper.ProveedorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProveedorRepositoryAdapter implements ProveedorGateway {

    private final ProveedorJpaRepository proveedorJpaRepository;
    private final ProveedorMapper proveedorMapper;

    @Override
    public Optional<Proveedor> findById(Integer idProveedor) {
        return proveedorJpaRepository.findById(idProveedor).map(proveedorMapper::toDomain);
    }

    @Override
    public Optional<Proveedor> findByNit(String nit) {
        return proveedorJpaRepository.findByNit(nit).map(proveedorMapper::toDomain);
    }

    @Override
    public List<Proveedor> findAll() {
        return proveedorJpaRepository.findAll().stream().map(proveedorMapper::toDomain).toList();
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        ProveedorData entity = proveedorMapper.toEntity(proveedor);
        return proveedorMapper.toDomain(proveedorJpaRepository.save(entity));
    }

    @Override
    public boolean existsProductosByProveedorId(Integer idProveedor) {
        return proveedorJpaRepository.existsByProductoProveedors_Id_IdProveedor(idProveedor);
    }
}