package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.catalog.infrastructure.driver_adapter.entity.CategoriaServicioData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaServicioJpaRepository extends JpaRepository<CategoriaServicioData, Integer> {

    boolean existsByIdCategoriaServicioAndServiciosIsNotEmpty(Integer idCategoriaServicio);

}