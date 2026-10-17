package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.transaction.domain.gateway.VentaGateway;
import com.invengest.transaction.domain.model.Venta;
import com.invengest.transaction.infrastructure.driver_adapter.entity.VentaData;
import com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces.VentaJpaRepository;
import com.invengest.transaction.infrastructure.mapper.VentaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VentaRepositoryAdapter implements VentaGateway {

    private final VentaJpaRepository ventaJpaRepository;
    private final VentaMapper ventaMapper;

    @Override
    public Venta save(Venta venta) {
        VentaData entity = ventaMapper.toEntity(venta);
        if (entity.getDetalles() != null) {
            entity.getDetalles().forEach(detalle -> detalle.setVenta(entity));
        }
        VentaData saved = ventaJpaRepository.save(entity);
        return ventaMapper.toDomain(saved);
    }

    @Override
    public Optional<Venta> findById(Integer idVenta) {
        return ventaJpaRepository.findByIdWithDetails(idVenta)
                .or(() -> ventaJpaRepository.findById(idVenta))
                .map(ventaMapper::toDomain);
    }

    @Override
    public List<Venta> findAll() {
        return ventaJpaRepository.findAll().stream()
                .map(ventaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return ventaJpaRepository.findByFechaBetween(inicio, fin).stream()
                .map(ventaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Venta> findByIdUsuario(Integer idUsuario) {
        return ventaJpaRepository.findByIdUsuario(idUsuario).stream()
                .map(ventaMapper::toDomain)
                .toList();
    }
}
