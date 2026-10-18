package com.invengest.analytics.domain.gateway;

import com.invengest.analytics.domain.model.ResumenVentaDiaria;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResumenVentaGateway {
    Optional<ResumenVentaDiaria> findByFecha(LocalDate fecha);
    List<ResumenVentaDiaria> findByFechaBetween(LocalDate inicio, LocalDate fin);
    List<ResumenVentaDiaria> findAll();
    ResumenVentaDiaria save(ResumenVentaDiaria resumen);
}
