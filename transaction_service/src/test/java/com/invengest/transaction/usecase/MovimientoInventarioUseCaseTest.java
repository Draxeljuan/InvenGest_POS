package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.BusinessRuleException;
import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.gateway.MovimientoInventarioGateway;
import com.invengest.transaction.domain.gateway.TipoMovimientoGateway;
import com.invengest.transaction.domain.model.MovimientoInventario;
import com.invengest.transaction.domain.model.TipoMovimiento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoInventarioUseCaseTest {

    @Mock
    private MovimientoInventarioGateway movimientoInventarioGateway;

    @Mock
    private TipoMovimientoGateway tipoMovimientoGateway;

    @InjectMocks
    private MovimientoInventarioUseCase useCase;

    @Test
    @DisplayName("Registrar movimiento de inventario exitosamente")
    void registrarMovimiento_Exitoso() {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdProducto("PROD-001");
        movimiento.setIdUsuario(1);
        movimiento.setCantidad(5);
        movimiento.setTipoMovimiento(new TipoMovimiento(1, null));
        movimiento.setObservacion("Ingreso por Compra Factura #123");

        TipoMovimiento tipo = new TipoMovimiento(1, "Ingreso por Compra");
        when(tipoMovimientoGateway.findById(1)).thenReturn(Optional.of(tipo));
        when(movimientoInventarioGateway.save(any())).thenAnswer(i -> i.getArgument(0));

        MovimientoInventario resultado = useCase.registrarMovimiento(movimiento);

        assertNotNull(resultado);
        assertEquals("PROD-001", resultado.getIdProducto());
        assertEquals("Ingreso por Compra", resultado.getTipoMovimiento().getNombre());
        verify(movimientoInventarioGateway).save(any());
    }

    @Test
    @DisplayName("Fallo al registrar movimiento con cantidad menor o igual a cero")
    void registrarMovimiento_CantidadInvalida() {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdProducto("PROD-001");
        movimiento.setIdUsuario(1);
        movimiento.setCantidad(0);
        movimiento.setTipoMovimiento(new TipoMovimiento(1, null));

        assertThrows(BusinessRuleException.class, () -> useCase.registrarMovimiento(movimiento));
        verify(movimientoInventarioGateway, never()).save(any());
    }

    @Test
    @DisplayName("Fallo al registrar movimiento con tipo inexistente")
    void registrarMovimiento_TipoInexistente() {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdProducto("PROD-001");
        movimiento.setIdUsuario(1);
        movimiento.setCantidad(5);
        movimiento.setTipoMovimiento(new TipoMovimiento(99, null));

        when(tipoMovimientoGateway.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.registrarMovimiento(movimiento));
    }

    @Test
    @DisplayName("Obtener Kardex por producto")
    void obtenerKardexPorProducto_Exitoso() {
        MovimientoInventario m1 = new MovimientoInventario();
        m1.setIdProducto("PROD-001");
        when(movimientoInventarioGateway.findByIdProducto("PROD-001")).thenReturn(List.of(m1));

        List<MovimientoInventario> resultado = useCase.obtenerKardexPorProducto("PROD-001");
        assertEquals(1, resultado.size());
    }
}
