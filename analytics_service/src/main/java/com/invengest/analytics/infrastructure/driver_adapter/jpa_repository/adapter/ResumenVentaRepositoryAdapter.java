package com.invengest.analytics.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.analytics.domain.gateway.ResumenVentaGateway;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.infrastructure.driver_adapter.entity.ResumenVentaDiariaData;
import com.invengest.analytics.infrastructure.driver_adapter.jpa_repository.interfaces.ResumenVentaDiariaJpaRepository;
import com.invengest.analytics.infrastructure.mapper.ResumenVentaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumenVentaRepositoryAdapter implements ResumenVentaGateway {

    private final ResumenVentaDiariaJpaRepository jpaRepository;
    private final ResumenVentaMapper mapper;

    @Override
    public Optional<ResumenVentaDiaria> findByFecha(LocalDate fecha) {
        return jpaRepository.findById(fecha)
                .map(mapper::toDomain);
    }

    @Override
    public List<ResumenVentaDiaria> findByFechaBetween(LocalDate inicio, LocalDate fin) {
        return jpaRepository.findByFechaBetweenOrderByFechaAsc(inicio, fin).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ResumenVentaDiaria> findAll() {
        return jpaRepository.findAllByOrderByFechaDesc().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public ResumenVentaDiaria save(ResumenVentaDiaria resumen) {
        ResumenVentaDiariaData entity = mapper.toEntity(resumen);
        ResumenVentaDiariaData saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
