package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.gateway.MetodoPagoGateway;
import com.invengest.transaction.domain.model.MetodoPago;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetodoPagoUseCaseTest {

    @Mock
    private MetodoPagoGateway metodoPagoGateway;

    @InjectMocks
    private MetodoPagoUseCase metodoPagoUseCase;

    @Test
    @DisplayName("Obtener método de pago por ID")
    void obtenerPorId_Exitoso() {
        MetodoPago mp = new MetodoPago(1, "Efectivo");
        when(metodoPagoGateway.findById(1)).thenReturn(Optional.of(mp));

        MetodoPago res = metodoPagoUseCase.obtenerPorId(1);
        assertEquals("Efectivo", res.getNombre());
    }

    @Test
    @DisplayName("Obtener método de pago no existente lanza EntityNotFoundException")
    void obtenerPorId_NoExiste_LanzaException() {
        when(metodoPagoGateway.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> metodoPagoUseCase.obtenerPorId(99));
    }

    @Test
    @DisplayName("Listar todos los métodos de pago")
    void listarTodos_Exitoso() {
        when(metodoPagoGateway.findAll()).thenReturn(List.of(new MetodoPago(1, "Efectivo"), new MetodoPago(2, "Transferencia")));

        List<MetodoPago> res = metodoPagoUseCase.listarTodos();
        assertEquals(2, res.size());
    }
}
