package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.gateway.VentaGateway;
import com.invengest.transaction.domain.model.Venta;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ConsultarVentaUseCase {

    private final VentaGateway ventaGateway;

    public Venta obtenerPorId(Integer idVenta) {
        return ventaGateway.findById(idVenta)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + idVenta));
    }

    public List<Venta> listarTodas() {
        return ventaGateway.findAll();
    }

    public List<Venta> filtrarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaGateway.findByFechaBetween(inicio, fin);
    }

    public List<Venta> listarPorUsuario(Integer idUsuario) {
        return ventaGateway.findByIdUsuario(idUsuario);
    }
}
