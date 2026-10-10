package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.catalog.domain.gateway.ServicioGateway;
import com.invengest.catalog.domain.model.Servicio;
import com.invengest.catalog.infrastructure.driver_adapter.entity.ServicioData;
import com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces.ServicioJpaRepository;
import com.invengest.catalog.infrastructure.mapper.ServicioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServicioRepositoryAdapter implements ServicioGateway {

    private final ServicioJpaRepository servicioJpaRepository;
    private final ServicioMapper servicioMapper;

    @Override
    public Optional<Servicio> findById(Integer idServicio) {
        return servicioJpaRepository.findById(idServicio).map(servicioMapper::toDomain);
    }

    @Override
    public List<Servicio> findAll() {
        return servicioJpaRepository.findAll().stream().map(servicioMapper::toDomain).toList();
    }

    @Override
    public List<Servicio> findByCategoriaServicioId(Integer idCategoriaServicio) {
        return servicioJpaRepository.findByCategoriaServicio_IdCategoriaServicio(idCategoriaServicio)
                .stream().map(servicioMapper::toDomain).toList();
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return servicioJpaRepository.existsByNombre(nombre);
    }

    @Override
    public Servicio save(Servicio servicio) {
        ServicioData entity = servicioMapper.toEntity(servicio);
        return servicioMapper.toDomain(servicioJpaRepository.save(entity));
    }


}