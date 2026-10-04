package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.ServicioRepository;
import com.invengest.catalog.domain.model.Servicio;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ServicioUseCase {

    private final ServicioRepository servicioRepository;

    public Servicio registrar(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public Servicio actualizar(Servicio servicio) {
        if (servicio.getIdServicio() == null || servicioRepository.findById(servicio.getIdServicio()).isEmpty()) {
            throw new EntityNotFoundException("El servicio a actualizar no existe.");
        }
        return servicioRepository.save(servicio);
    }

    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }

    public Servicio obtenerPorId(Integer idServicio) {
        return servicioRepository.findById(idServicio)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con ID: " + idServicio));
    }

    public List<Servicio> obtenerPorCategoria(Integer idCategoriaServicio) {
        return servicioRepository.findByCategoriaServicioId(idCategoriaServicio);
    }

    public void eliminarPorId(Integer idServicio) {

        if (servicioRepository.findById(idServicio).isEmpty()) {
            throw new EntityNotFoundException("El servicio no existe.");
        }
        servicioRepository.deleteById(idServicio);
    }
}