package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.gateway.MetodoPagoGateway;
import com.invengest.transaction.domain.model.MetodoPago;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MetodoPagoUseCase {

    private final MetodoPagoGateway metodoPagoGateway;

    public MetodoPago obtenerPorId(Integer idMetodoPago) {
        return metodoPagoGateway.findById(idMetodoPago)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado con ID: " + idMetodoPago));
    }

    public List<MetodoPago> listarTodos() {
        return metodoPagoGateway.findAll();
    }
}
