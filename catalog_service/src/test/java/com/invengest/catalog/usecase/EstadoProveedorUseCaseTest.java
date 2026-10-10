package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.EstadoProveedorGateway;
import com.invengest.catalog.domain.model.EstadoProveedor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoProveedorUseCaseTest {

    @Mock
    private EstadoProveedorGateway estadoProveedorGateway;

    @InjectMocks
    private EstadoProveedorUseCase estadoProveedorUseCase;

    @Test
    void obtenerPorId_Exitoso() {
        when(estadoProveedorGateway.findById(1)).thenReturn(Optional.of(new EstadoProveedor(1, "Activo")));

        EstadoProveedor resultado = estadoProveedorUseCase.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals("Activo", resultado.getNombre());
    }
}
