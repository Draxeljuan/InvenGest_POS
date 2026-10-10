package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.catalog.infrastructure.driver_adapter.entity.EstadoProveedorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoProveedorJpaRepository extends JpaRepository<EstadoProveedorData, Integer> {
    Optional<EstadoProveedorData> findByNombre(String nombre);
}