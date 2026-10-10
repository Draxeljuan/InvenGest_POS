package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaServicioGateway;
import com.invengest.catalog.domain.model.CategoriaServicio;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaServicioUseCase {

    private final CategoriaServicioGateway categoriaServicioGateway;


    public CategoriaServicio crear(CategoriaServicio categoriaServicio) {
        return categoriaServicioGateway.save(categoriaServicio);
    }

    public CategoriaServicio actualizar(CategoriaServicio categoriaServicio) {
        if (categoriaServicio.getIdCategoriaServicio() == null ||
                categoriaServicioGateway.findById(categoriaServicio.getIdCategoriaServicio()).isEmpty()) {
            throw new EntityNotFoundException("La categoría de servicio a actualizar no existe.");
        }
        return categoriaServicioGateway.save(categoriaServicio);
    }

    public List<CategoriaServicio> obtenerTodas() {
        return categoriaServicioGateway.findAll();
    }

    public CategoriaServicio obtenerPorId(Integer id) {
        return categoriaServicioGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría de servicio no encontrada con ID: " + id));
    }

    public void eliminarPorId(Integer id) {
        categoriaServicioGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La categoría de servicio no existe."));

        if (categoriaServicioGateway.existsServiciosByCategoriaServicioId(id)) {
            throw new EntityInUseException("No se puede eliminar la categoría porque contiene servicios vinculados.");
        }

        categoriaServicioGateway.deleteById(id);
    }
}