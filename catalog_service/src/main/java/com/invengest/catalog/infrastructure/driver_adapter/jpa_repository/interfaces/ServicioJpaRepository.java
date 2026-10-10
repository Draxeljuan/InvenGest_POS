package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.catalog.infrastructure.driver_adapter.entity.ServicioData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioJpaRepository extends JpaRepository<ServicioData, Integer> {

    List<ServicioData> findByCategoriaServicio_IdCategoriaServicio(Integer idCategoriaServicio);
    boolean existsByNombre(String nombre);

}