package com.invengest.analytics.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.analytics.infrastructure.driver_adapter.entity.ResumenVentaDiariaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ResumenVentaDiariaJpaRepository extends JpaRepository<ResumenVentaDiariaData, LocalDate> {
    List<ResumenVentaDiariaData> findByFechaBetweenOrderByFechaAsc(LocalDate inicio, LocalDate fin);
    List<ResumenVentaDiariaData> findAllByOrderByFechaDesc();
}
