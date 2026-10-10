package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.gateway.CategoriaGateway;
import com.invengest.catalog.domain.model.Categoria;
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
class CategoriaUseCaseTest {

    @Mock
    private CategoriaGateway categoriaGateway;

    @InjectMocks
    private CategoriaUseCase categoriaUseCase;

    private Categoria categoriaValida;

    @BeforeEach
    void setUp() {
        categoriaValida = new Categoria();
        categoriaValida.setIdCategoria(1);
        categoriaValida.setNombre("Electrónica");
    }

    @Test
    void crear_Exitoso() {
        when(categoriaGateway.existsByNombre("Electrónica")).thenReturn(false);
        when(categoriaGateway.save(any(Categoria.class))).thenReturn(categoriaValida);

        Categoria creada = categoriaUseCase.crear(categoriaValida);

        assertNotNull(creada);
        assertEquals("Electrónica", creada.getNombre());
        verify(categoriaGateway).save(categoriaValida);
    }

    @Test
    void crear_FallaPorNombreDuplicado() {
        when(categoriaGateway.existsByNombre("Electrónica")).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> categoriaUseCase.crear(categoriaValida));
        verify(categoriaGateway, never()).save(any());
    }

    @Test
    void eliminar_Exitoso() {
        when(categoriaGateway.findById(1)).thenReturn(Optional.of(categoriaValida));
        when(categoriaGateway.existsProductosByCategoriaId(1)).thenReturn(false);

        categoriaUseCase.eliminarPorId(1);

        verify(categoriaGateway).deleteById(1);
    }

    @Test
    void eliminar_FallaPorqueTieneProductos() {
        when(categoriaGateway.findById(1)).thenReturn(Optional.of(categoriaValida));
        when(categoriaGateway.existsProductosByCategoriaId(1)).thenReturn(true);

        assertThrows(EntityInUseException.class, () -> categoriaUseCase.eliminarPorId(1));
        verify(categoriaGateway, never()).deleteById(anyInt());
    }
}
