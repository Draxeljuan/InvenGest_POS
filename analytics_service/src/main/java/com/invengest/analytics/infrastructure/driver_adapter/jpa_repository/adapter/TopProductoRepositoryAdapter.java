package com.invengest.analytics.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.domain.model.TopProducto;
import com.invengest.analytics.infrastructure.driver_adapter.entity.TopProductoData;
import com.invengest.analytics.infrastructure.driver_adapter.jpa_repository.interfaces.TopProductoJpaRepository;
import com.invengest.analytics.infrastructure.mapper.TopProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopProductoRepositoryAdapter implements TopProductoGateway {

    private final TopProductoJpaRepository jpaRepository;
    private final TopProductoMapper mapper;

    @Override
    public List<TopProducto> findByFecha(LocalDate fecha) {
        return jpaRepository.findByFechaOrderByCantidadVendidaDesc(fecha).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<TopProducto> findByFechaBetween(LocalDate inicio, LocalDate fin) {
        return jpaRepository.findByFechaBetweenOrderByCantidadVendidaDesc(inicio, fin).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<TopProducto> findTopNByFechaBetween(LocalDate inicio, LocalDate fin, int limit) {
        return jpaRepository.findTopGroupedByFechaBetween(inicio, fin, PageRequest.of(0, limit)).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<TopProducto> findByIdProductoAndFecha(String idProducto, LocalDate fecha) {
        return jpaRepository.findFirstByIdProductoAndFecha(idProducto, fecha)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public TopProducto save(TopProducto topProducto) {
        TopProductoData entity = mapper.toEntity(topProducto);
        TopProductoData saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
