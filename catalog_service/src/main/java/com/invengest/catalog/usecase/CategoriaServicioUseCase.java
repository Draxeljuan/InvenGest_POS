package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaServicioRepository;
import com.invengest.catalog.domain.model.CategoriaServicio;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaServicioUseCase {

    private final CategoriaServicioRepository categoriaServicioRepository;


    public CategoriaServicio crear(CategoriaServicio categoriaServicio) {
        return categoriaServicioRepository.save(categoriaServicio);
    }

    public CategoriaServicio actualizar(CategoriaServicio categoriaServicio) {
        if (categoriaServicio.getIdCategoriaServicio() == null ||
                categoriaServicioRepository.findById(categoriaServicio.getIdCategoriaServicio()).isEmpty()) {
            throw new EntityNotFoundException("La categoría de servicio a actualizar no existe.");
        }
        return categoriaServicioRepository.save(categoriaServicio);
    }

    public List<CategoriaServicio> obtenerTodas() {
        return categoriaServicioRepository.findAll();
    }

    public CategoriaServicio obtenerPorId(Integer id) {
        return categoriaServicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría de servicio no encontrada con ID: " + id));
    }

    public void eliminarPorId(Integer id) {
        if (categoriaServicioRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("La categoría de servicio no existe.");
        }
        if (categoriaServicioRepository.existsServiciosByCategoriaServicioId(id)) {
            throw new EntityInUseException("No se puede eliminar la categoría porque contiene servicios vinculados.");
        }
        categoriaServicioRepository.deleteById(id);
    }
}