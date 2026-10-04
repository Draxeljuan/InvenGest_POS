package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaGateway;
import com.invengest.catalog.domain.model.Categoria;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaUseCase { // Por crear validaciones de negocio más especificas

    private final CategoriaGateway categoriaGateway;

    public Categoria crear(Categoria categoria) {
        return categoriaGateway.save(categoria);
    }

    public Categoria actualizar(Categoria categoria) {
        if (categoria.getIdCategoria() == null || categoriaGateway.findById(categoria.getIdCategoria()).isEmpty()) {
            throw new EntityNotFoundException("La categoría con ID " + categoria.getIdCategoria() + " no existe.");
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