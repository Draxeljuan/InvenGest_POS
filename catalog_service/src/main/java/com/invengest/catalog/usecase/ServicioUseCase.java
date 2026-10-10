package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaServicioGateway;
import com.invengest.catalog.domain.gateway.ServicioGateway;
import com.invengest.catalog.domain.model.CategoriaServicio;
import com.invengest.catalog.domain.model.Servicio;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ServicioUseCase {

    private final ServicioGateway servicioGateway;
    private final CategoriaServicioGateway categoriaServicioGateway;

    public Servicio registrar(Servicio servicio) {
        if (servicioGateway.existsByNombre(servicio.getNombre())) {
            throw new DuplicateEntityException("Ya existe un servicio con el nombre: " + servicio.getNombre());
        }
        validarCategoria(servicio);
        return servicioGateway.save(servicio);
    }

    public Servicio actualizar(Servicio servicio) {
        Servicio existente = servicioGateway.findById(servicio.getIdServicio())
            .orElseThrow(() -> new EntityNotFoundException("El servicio a actualizar no existe."));
            
        if (!existente.getNombre().equalsIgnoreCase(servicio.getNombre()) &&
            servicioGateway.existsByNombre(servicio.getNombre())) {
            throw new DuplicateEntityException("Ya existe un servicio con el nombre: " + servicio.getNombre());
        }
        validarCategoria(servicio);
        return servicioGateway.save(servicio);
    }

    private void validarCategoria(Servicio servicio) {
        if (servicio.getCategoriaServicio() == null || servicio.getCategoriaServicio().getIdCategoriaServicio() == null ||
            categoriaServicioGateway.findById(servicio.getCategoriaServicio().getIdCategoriaServicio()).isEmpty()) {
            throw new EntityNotFoundException("La categoría de servicio asignada no existe.");
        }
    }

    public List<Servicio> obtenerTodos() {
        return servicioGateway.findAll();
    }

    public Servicio obtenerPorId(Integer idServicio) {
        return servicioGateway.findById(idServicio)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con ID: " + idServicio));
    }

    public List<Servicio> obtenerPorCategoria(Integer idCategoriaServicio) {
        return servicioGateway.findByCategoriaServicioId(idCategoriaServicio);
    }

    public void eliminacionLogica(Integer idServicio) {
        Servicio servicioADeshabilitar = servicioGateway.findById(idServicio)
                .orElseThrow(() -> new EntityNotFoundException("El servicio no existe"));

        CategoriaServicio categoriaDeshabilitado = categoriaServicioGateway.findById(1)
                .orElseThrow(() -> new IllegalStateException("La categoria Deshabilitado (ID 1) no esta configurada en el sistema"));

        servicioADeshabilitar.setCategoriaServicio(categoriaDeshabilitado);

        servicioGateway.save(servicioADeshabilitar);
    }
}