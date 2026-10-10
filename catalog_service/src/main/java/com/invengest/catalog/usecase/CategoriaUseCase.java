package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaGateway;
import com.invengest.catalog.domain.model.Categoria;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaUseCase {

    private final CategoriaGateway categoriaGateway;

    public Categoria crear(Categoria categoria) {
        if (categoriaGateway.existsByNombre(categoria.getNombre())) {
            throw new DuplicateEntityException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaGateway.save(categoria);
    }

    public Categoria actualizar(Categoria categoria) {
        Categoria existente = categoriaGateway.findById(categoria.getIdCategoria())
            .orElseThrow(() -> new EntityNotFoundException("La categoría con ID " + categoria.getIdCategoria() + " no existe."));
            
        if (!existente.getNombre().equalsIgnoreCase(categoria.getNombre()) && 
            categoriaGateway.existsByNombre(categoria.getNombre())) {
            throw new DuplicateEntityException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaGateway.save(categoria);
    }

    public List<Categoria> obtenerTodas() {
        return categoriaGateway.findAll();
    }

    public Categoria obtenerPorId(Integer id) {
        return categoriaGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
    }

    public void eliminarPorId(Integer id) {
        if (categoriaGateway.findById(id).isEmpty()) {
            throw new EntityNotFoundException("La categoría no existe.");
        }
        if (categoriaGateway.existsProductosByCategoriaId(id)) {
            throw new EntityInUseException("No se puede eliminar la categoría porque tiene productos asociados.");
        }
        categoriaGateway.deleteById(id);
    }
}