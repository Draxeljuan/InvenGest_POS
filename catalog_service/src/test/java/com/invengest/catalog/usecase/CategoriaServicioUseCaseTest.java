package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaServicioGateway;
import com.invengest.catalog.domain.model.CategoriaServicio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServicioUseCaseTest {

    @Mock
    private CategoriaServicioGateway categoriaServicioGateway;

    @InjectMocks
    private CategoriaServicioUseCase categoriaServicioUseCase;

    @Test
    void crear_Exitoso() {
        CategoriaServicio categoria = new CategoriaServicio(1, "CatServicio");
        when(categoriaServicioGateway.save(any())).thenReturn(categoria);

        CategoriaServicio resultado = categoriaServicioUseCase.crear(categoria);

        assertNotNull(resultado);
        assertEquals("CatServicio", resultado.getNombre());
        verify(categoriaServicioGateway).save(categoria);
    }
}
