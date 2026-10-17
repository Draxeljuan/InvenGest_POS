package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.transaction.infrastructure.driver_adapter.entity.TipoMovimientoData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoMovimientoJpaRepository extends JpaRepository<TipoMovimientoData, Integer> {
    Optional<TipoMovimientoData> findByNombre(String nombre);
}
