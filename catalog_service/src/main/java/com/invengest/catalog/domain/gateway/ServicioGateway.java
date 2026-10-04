package com.invengest.catalog.domain.gateway;

import com.invengest.catalog.domain.model.Servicio;
import java.util.List;
import java.util.Optional;

public interface ServicioGateway {
    Optional<Servicio> findById(Integer idServicio);
    List<Servicio> findAll();
    List<Servicio> findByCategoriaServicioId(Integer idCategoriaServicio);
    Servicio save(Servicio servicio);
    void deleteById(Integer idServicio);
}