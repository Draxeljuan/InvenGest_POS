package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.gateway.VentaGateway;
import com.invengest.transaction.domain.model.Venta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarVentaUseCaseTest {

    @Mock
    private VentaGateway ventaGateway;

    @InjectMocks
    private ConsultarVentaUseCase consultarVentaUseCase;

    @Test
    @DisplayName("Obtener venta por ID exitosamente")
    void obtenerPorId_Exitoso() {
        Venta venta = new Venta();
        venta.setIdVenta(1);
        when(ventaGateway.findById(1)).thenReturn(Optional.of(venta));

        Venta resultado = consultarVentaUseCase.obtenerPorId(1);
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdVenta());
    }

    @Test
    @DisplayName("Obtener venta por ID inexistente lanza EntityNotFoundException")
    void obtenerPorId_NoExiste_LanzaException() {
        when(ventaGateway.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> consultarVentaUseCase.obtenerPorId(99));
    }

    @Test
    @DisplayName("Listar todas las ventas")
    void listarTodas_Exitoso() {
        Venta v1 = new Venta();
        v1.setIdVenta(1);
        Venta v2 = new Venta();
        v2.setIdVenta(2);

        when(ventaGateway.findAll()).thenReturn(List.of(v1, v2));

        List<Venta> resultado = consultarVentaUseCase.listarTodas();
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Filtrar ventas por rango de fechas")
    void filtrarPorFechas_Exitoso() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now();

        Venta v1 = new Venta();
        v1.setIdVenta(1);

        when(ventaGateway.findByFechaBetween(inicio, fin)).thenReturn(List.of(v1));

        List<Venta> resultado = consultarVentaUseCase.filtrarPorFechas(inicio, fin);
        assertEquals(1, resultado.size());
    }
}
