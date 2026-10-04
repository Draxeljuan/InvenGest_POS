package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaRepository;
import com.invengest.catalog.domain.model.Categoria;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaUseCase { // Por crear validaciones de negocio más especificas

    private final CategoriaRepository categoriaRepository;

    public Categoria crear(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Categoria categoria) {
        if (categoria.getIdCategoria() == null || categoriaRepository.findById(categoria.getIdCategoria()).isEmpty()) {
            throw new EntityNotFoundException("La categoría con ID " + categoria.getIdCategoria() + " no existe.");
        }
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
    }

    public void eliminarPorId(Integer id) {
        if (categoriaRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("La categoría no existe.");
        }
        if (categoriaRepository.existsProductosByCategoriaId(id)) {
            throw new EntityInUseException("No se puede eliminar la categoría porque tiene productos asociados.");
        }
        categoriaRepository.deleteById(id);
    }
}