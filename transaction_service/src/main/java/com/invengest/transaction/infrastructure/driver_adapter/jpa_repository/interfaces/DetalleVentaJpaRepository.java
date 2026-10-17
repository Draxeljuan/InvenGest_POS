package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.transaction.infrastructure.driver_adapter.entity.DetalleVentaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaJpaRepository extends JpaRepository<DetalleVentaData, Integer> {
    List<DetalleVentaData> findByVenta_IdVenta(Integer idVenta);
}
