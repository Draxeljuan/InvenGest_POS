package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.catalog.infrastructure.driver_adapter.entity.ProveedorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorJpaRepository extends JpaRepository<ProveedorData, Integer> {
    Optional<ProveedorData> findByNit(String nit);

    boolean existsByProductoProveedors_Id_IdProveedor(Integer idProveedor);
}