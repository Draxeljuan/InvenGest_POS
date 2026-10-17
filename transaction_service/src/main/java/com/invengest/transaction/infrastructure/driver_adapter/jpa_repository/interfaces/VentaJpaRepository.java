package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.transaction.infrastructure.driver_adapter.entity.VentaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaJpaRepository extends JpaRepository<VentaData, Integer> {

    List<VentaData> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<VentaData> findByIdUsuario(Integer idUsuario);

    @Query("SELECT v FROM VentaData v LEFT JOIN FETCH v.detalles LEFT JOIN FETCH v.cliente LEFT JOIN FETCH v.metodoPago WHERE v.idVenta = :id")
    java.util.Optional<VentaData> findByIdWithDetails(@Param("id") Integer id);
}
