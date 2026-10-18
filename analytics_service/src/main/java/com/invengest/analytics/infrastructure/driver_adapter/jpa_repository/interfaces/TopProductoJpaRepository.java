package com.invengest.analytics.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.analytics.infrastructure.driver_adapter.entity.TopProductoData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TopProductoJpaRepository extends JpaRepository<TopProductoData, Integer> {

    List<TopProductoData> findByFechaOrderByCantidadVendidaDesc(LocalDate fecha);

    List<TopProductoData> findByFechaBetweenOrderByCantidadVendidaDesc(LocalDate inicio, LocalDate fin);

    Optional<TopProductoData> findFirstByIdProductoAndFecha(String idProducto, LocalDate fecha);

    @Query("""
            SELECT new com.invengest.analytics.infrastructure.driver_adapter.entity.TopProductoData(
                MAX(t.idTop),
                t.idProducto,
                MAX(t.nombreProducto),
                CAST(SUM(t.cantidadVendida) AS integer),
                MAX(t.fecha)
            )
            FROM TopProductoData t
            WHERE t.fecha BETWEEN :inicio AND :fin
            GROUP BY t.idProducto
            ORDER BY SUM(t.cantidadVendida) DESC
            """)
    List<TopProductoData> findTopGroupedByFechaBetween(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin,
            Pageable pageable);
}
