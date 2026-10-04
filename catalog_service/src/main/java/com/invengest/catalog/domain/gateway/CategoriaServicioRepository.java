package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.CategoriaServicio;
import java.util.List;
import java.util.Optional;

public interface CategoriaServicioRepository {
    Optional<CategoriaServicio> findById(Integer idCategoriaServicio);
    List<CategoriaServicio> findAll();
    CategoriaServicio save(CategoriaServicio categoriaServicio);
    void deleteById(Integer idCategoriaServicio);
    boolean existsServiciosByCategoriaServicioId(Integer idCategoriaServicio);

}