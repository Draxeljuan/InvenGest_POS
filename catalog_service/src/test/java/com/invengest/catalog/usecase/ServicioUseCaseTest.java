package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaServicioGateway;
import com.invengest.catalog.domain.gateway.ServicioGateway;
import com.invengest.catalog.domain.model.CategoriaServicio;
import com.invengest.catalog.domain.model.Servicio;
import org.junit.jupiter.api.BeforeEach;
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
class ServicioUseCaseTest {

    @Mock
    private ServicioGateway servicioGateway;

    @Mock
    private CategoriaServicioGateway categoriaServicioGateway;

    @InjectMocks
    private ServicioUseCase servicioUseCase;

    private Servicio servicioValido;

    @BeforeEach
    void setUp() {
        CategoriaServicio categoriaServicio = new CategoriaServicio();
        categoriaServicio.setIdCategoriaServicio(1);

        servicioValido = new Servicio();
        servicioValido.setIdServicio(1);
        servicioValido.setNombre("Mantenimiento");
        servicioValido.setCategoriaServicio(categoriaServicio);
    }

    @Test
    void registrar_Exitoso() {
        when(servicioGateway.existsByNombre("Mantenimiento")).thenReturn(false);
        when(categoriaServicioGateway.findById(1)).thenReturn(Optional.of(new CategoriaServicio()));
        when(servicioGateway.save(any(Servicio.class))).thenReturn(servicioValido);

        Servicio registrado = servicioUseCase.registrar(servicioValido);

        assertNotNull(registrado);
        assertEquals("Mantenimiento", registrado.getNombre());
        verify(servicioGateway).save(servicioValido);
    }

    @Test
    void registrar_LanzaExcepcion_NombreDuplicado() {
        when(servicioGateway.existsByNombre("Mantenimiento")).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> servicioUseCase.registrar(servicioValido));
        verify(servicioGateway, never()).save(any());
    }
}
