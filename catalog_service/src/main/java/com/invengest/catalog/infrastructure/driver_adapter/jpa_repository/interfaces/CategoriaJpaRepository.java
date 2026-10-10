package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.catalog.infrastructure.driver_adapter.entity.CategoriaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaJpaRepository extends JpaRepository<CategoriaData, Integer> {
    boolean existsByNombre(String nombre);
}