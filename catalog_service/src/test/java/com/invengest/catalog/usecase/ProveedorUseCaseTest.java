package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.EstadoProveedorGateway;
import com.invengest.catalog.domain.gateway.ProveedorGateway;
import com.invengest.catalog.domain.model.EstadoProveedor;
import com.invengest.catalog.domain.model.Proveedor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorUseCaseTest {

    @Mock
    private ProveedorGateway proveedorGateway;
    @Mock
    private EstadoProveedorGateway estadoProveedorGateway;

    @InjectMocks
    private ProveedorUseCase proveedorUseCase;

    @Test
    void crear_Exitoso() {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Proveedor 1");

        when(proveedorGateway.save(any())).thenReturn(proveedor);
        Proveedor resultado = proveedorUseCase.crear(proveedor);

        assertNotNull(resultado);
        verify(proveedorGateway).save(proveedor);
    }
}
