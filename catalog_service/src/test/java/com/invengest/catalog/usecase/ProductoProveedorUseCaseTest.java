package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.gateway.ProductoProveedorGateway;
import com.invengest.catalog.domain.model.ProductoProveedor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoProveedorUseCaseTest {

    @Mock
    private ProductoProveedorGateway productoProveedorGateway;

    @InjectMocks
    private ProductoProveedorUseCase productoProveedorUseCase;

    @Test
    void asociar_Exitoso() {
        ProductoProveedor pp = new ProductoProveedor("PROD-1", 1, new BigDecimal("50.0"));
        when(productoProveedorGateway.save(any())).thenReturn(pp);

        ProductoProveedor resultado = productoProveedorUseCase.asociarOActualizar(pp);

        assertNotNull(resultado);
        verify(productoProveedorGateway).save(pp);
    }
}
