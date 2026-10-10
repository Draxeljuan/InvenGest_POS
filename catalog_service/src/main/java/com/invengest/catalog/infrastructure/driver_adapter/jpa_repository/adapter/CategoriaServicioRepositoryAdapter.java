package com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.adapter;

import com.invengest.catalog.domain.gateway.CategoriaServicioGateway;
import com.invengest.catalog.domain.model.CategoriaServicio;
import com.invengest.catalog.infrastructure.driver_adapter.entity.CategoriaServicioData;
import com.invengest.catalog.infrastructure.driver_adapter.jpa_repository.interfaces.CategoriaServicioJpaRepository;
import com.invengest.catalog.infrastructure.mapper.CategoriaServicioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoriaServicioRepositoryAdapter implements CategoriaServicioGateway {

    private final CategoriaServicioJpaRepository categoriaServicioJpaRepository;
    private final CategoriaServicioMapper categoriaServicioMapper;

    @Override
    public Optional<CategoriaServicio> findById(Integer idCategoriaServicio) {
        return categoriaServicioJpaRepository.findById(idCategoriaServicio).map(categoriaServicioMapper::toDomain);
    }

    @Override
    public List<CategoriaServicio> findAll() {
        return categoriaServicioJpaRepository.findAll().stream().map(categoriaServicioMapper::toDomain).toList();
    }

    @Override
    public CategoriaServicio save(CategoriaServicio categoriaServicio) {
        CategoriaServicioData entity = categoriaServicioMapper.toEntity(categoriaServicio);
        return categoriaServicioMapper.toDomain(categoriaServicioJpaRepository.save(entity));
    }

    @Override
    public void deleteById(Integer idCategoriaServicio) {
        categoriaServicioJpaRepository.deleteById(idCategoriaServicio);
    }

    @Override
    public boolean existsServiciosByCategoriaServicioId(Integer idCategoriaServicio) {
        return categoriaServicioJpaRepository.existsByIdCategoriaServicioAndServiciosIsNotEmpty(idCategoriaServicio);
    }
}