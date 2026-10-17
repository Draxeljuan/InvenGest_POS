package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.transaction.infrastructure.driver_adapter.entity.MovimientoInventarioData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoInventarioJpaRepository extends JpaRepository<MovimientoInventarioData, Integer> {

    List<MovimientoInventarioData> findByIdProductoOrderByFechaMovimientoDesc(String idProducto);

    List<MovimientoInventarioData> findByFechaMovimientoBetweenOrderByFechaMovimientoDesc(LocalDateTime inicio, LocalDateTime fin);
}
