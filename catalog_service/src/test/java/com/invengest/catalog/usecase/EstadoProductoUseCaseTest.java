package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.EstadoProductoGateway;
import com.invengest.catalog.domain.model.EstadoProducto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoProductoUseCaseTest {

    @Mock
    private EstadoProductoGateway estadoProductoGateway;

    @InjectMocks
    private EstadoProductoUseCase estadoProductoUseCase;

    @Test
    void obtenerPorId_Exitoso() {
        when(estadoProductoGateway.findById(1)).thenReturn(Optional.of(new EstadoProducto(1, "Activo")));

        EstadoProducto resultado = estadoProductoUseCase.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals("Activo", resultado.getNombre());
    }

    @Test
    void obtenerPorId_NoEncontrado() {
        when(estadoProductoGateway.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> estadoProductoUseCase.obtenerPorId(99));
    }
}
