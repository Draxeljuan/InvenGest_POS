package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.ServicioGateway;
import com.invengest.catalog.domain.model.Servicio;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ServicioUseCase {

    private final ServicioGateway servicioGateway;

    public Servicio registrar(Servicio servicio) {
        return servicioGateway.save(servicio);
    }

    public Servicio actualizar(Servicio servicio) {
        if (servicio.getIdServicio() == null || servicioGateway.findById(servicio.getIdServicio()).isEmpty()) {
            throw new EntityNotFoundException("El servicio a actualizar no existe.");
        }
        return servicioGateway.save(servicio);
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

    public void eliminarPorId(Integer idServicio) {

        if (servicioGateway.findById(idServicio).isEmpty()) {
            throw new EntityNotFoundException("El servicio no existe.");
        }
        servicioGateway.deleteById(idServicio);
    }
}