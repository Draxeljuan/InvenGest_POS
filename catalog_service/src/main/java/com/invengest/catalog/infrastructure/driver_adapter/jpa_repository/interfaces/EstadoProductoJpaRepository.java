package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.catalog.infrastructure.driver_adapter.entity.EstadoProductoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoProductoJpaRepository extends JpaRepository<EstadoProductoData, Integer> {
    Optional<EstadoProductoData> findByNombre(String nombre);
}