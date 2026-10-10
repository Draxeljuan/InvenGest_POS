package com.invengest.catalog.usecase;

import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.gateway.CategoriaGateway;
import com.invengest.catalog.domain.gateway.EstadoProductoGateway;
import com.invengest.catalog.domain.gateway.ProductoGateway;
import com.invengest.catalog.domain.model.Categoria;
import com.invengest.catalog.domain.model.EstadoProducto;
import com.invengest.catalog.domain.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoUseCaseTest {

    @Mock
    private ProductoGateway productoGateway;

    @Mock
    private EstadoProductoGateway estadoProductoGateway;

    @Mock
    private CategoriaGateway categoriaGateway;

    @InjectMocks
    private ProductoUseCase productoUseCase;

    private Producto productoValido;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1);

        productoValido = new Producto();
        productoValido.setCategoria(categoria);
        productoValido.setPrecioVenta(new BigDecimal("150.0"));
        productoValido.setCostoCompra(new BigDecimal("100.0"));
        productoValido.setStock(10);
        productoValido.setStockMinimo(5);
    }

    @Test
    void registrar_Exitoso() {
        when(categoriaGateway.findById(1)).thenReturn(Optional.of(new Categoria()));
        when(productoGateway.existsById(anyString())).thenReturn(false);
        when(productoGateway.save(any(Producto.class))).thenAnswer(i -> i.getArguments()[0]);

        Producto registrado = productoUseCase.registrar(productoValido);

        assertNotNull(registrado.getIdProducto());
        assertTrue(registrado.getIdProducto().startsWith("PROD-"));
        verify(productoGateway, times(1)).save(any(Producto.class));
    }

    @Test
    void registrar_LanzaExcepcion_CategoriaNoExiste() {
        when(categoriaGateway.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productoUseCase.registrar(productoValido));
        verify(productoGateway, never()).save(any());
    }

    @Test
    void registrar_LanzaExcepcion_PrecioInvalido() {
        when(categoriaGateway.findById(1)).thenReturn(Optional.of(new Categoria()));
        productoValido.setPrecioVenta(new BigDecimal("50.0"));
        productoValido.setCostoCompra(new BigDecimal("100.0"));

        assertThrows(IllegalArgumentException.class, () -> productoUseCase.registrar(productoValido));
        verify(productoGateway, never()).save(any());
    }

    @Test
    void descontarStock_Exitoso() {
        productoValido.setIdProducto("PROD-1234");
        when(productoGateway.findById("PROD-1234")).thenReturn(Optional.of(productoValido));
        when(productoGateway.save(any(Producto.class))).thenReturn(productoValido);

        Producto actualizado = productoUseCase.descontarStock("PROD-1234", 3);

        assertEquals(7, actualizado.getStock());
    }

    @Test
    void descontarStock_Agotado_CambiaEstado() {
        productoValido.setIdProducto("PROD-1234");
        productoValido.setStock(3);
        when(productoGateway.findById("PROD-1234")).thenReturn(Optional.of(productoValido));
        when(estadoProductoGateway.findById(3)).thenReturn(Optional.of(new EstadoProducto(3, "Agotado")));
        when(productoGateway.save(any(Producto.class))).thenReturn(productoValido);

        Producto actualizado = productoUseCase.descontarStock("PROD-1234", 3);

        assertEquals(0, actualizado.getStock());
        assertEquals("Agotado", actualizado.getEstado().getNombre());
    }
}
