package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaGateway {
    Optional<Categoria> findById(Integer idCategoria);
    List<Categoria> findAll();
    Categoria save(Categoria categoria);
    void deleteById(Integer idCategoria);
    boolean existsProductosByCategoriaId(Integer idCategoria);
}