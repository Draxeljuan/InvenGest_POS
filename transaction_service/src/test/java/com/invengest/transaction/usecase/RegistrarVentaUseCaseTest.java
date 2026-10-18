package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.BusinessRuleException;
import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.exception.InsufficientStockException;
import com.invengest.transaction.domain.gateway.*;
import com.invengest.transaction.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarVentaUseCaseTest {

    @Mock
    private VentaGateway ventaGateway;

    @Mock
    private CatalogGateway catalogGateway;

    @Mock
    private MovimientoInventarioGateway movimientoInventarioGateway;

    @Mock
    private ClienteGateway clienteGateway;

    @Mock
    private MetodoPagoGateway metodoPagoGateway;

    @Mock
    private TipoMovimientoGateway tipoMovimientoGateway;

    @Mock
    private AnalyticsGateway analyticsGateway;

    @InjectMocks
    private RegistrarVentaUseCase registrarVentaUseCase;

    private MetodoPago metodoPago;
    private Cliente cliente;
    private TipoMovimiento tipoMovimientoSalida;

    @BeforeEach
    void setUp() {
        metodoPago = new MetodoPago(1, "Efectivo");
        cliente = new Cliente();
        cliente.setIdCliente(10);
        cliente.setPrimerNombre("Carlos");
        cliente.setPrimerApellido("Perez");

        tipoMovimientoSalida = new TipoMovimiento(2, "Salida por Venta");
    }

    @Test
    @DisplayName("CU-04: Registrar venta exitosa con productos y validación de Kardex/Snapshots")
    void registrarVenta_Exitoso_SoloProductos() {
        // Arrange
        Venta venta = new Venta();
        venta.setIdUsuario(1);
        venta.setMetodoPago(new MetodoPago(1, null));
        venta.setCliente(new Cliente(10, null, null, null, null, null, null, null, null));

        List<DetalleVenta> items = new ArrayList<>();
        DetalleVenta item1 = new DetalleVenta();
        item1.setIdItem("PROD-001");
        item1.setTipoItem(TipoItem.PRODUCTO);
        item1.setCantidad(2);
        items.add(item1);
        venta.setDetalles(items);

        ProductoCatalogo productoCatalogo = new ProductoCatalogo("PROD-001", "Cuaderno 100 Hojas", new BigDecimal("5000.00"), 20, 5);

        when(metodoPagoGateway.findById(1)).thenReturn(Optional.of(metodoPago));
        when(clienteGateway.findById(10)).thenReturn(Optional.of(cliente));
        when(catalogGateway.obtenerProductoPorId("PROD-001")).thenReturn(Optional.of(productoCatalogo));
        when(tipoMovimientoGateway.findByNombre("Salida por Venta")).thenReturn(Optional.of(tipoMovimientoSalida));
        when(ventaGateway.save(any(Venta.class))).thenAnswer(invocation -> {
            Venta v = invocation.getArgument(0);
            v.setIdVenta(101);
            return v;
        });

        // Act
        Venta resultado = registrarVentaUseCase.registrarVenta(venta);

        // Assert
        assertNotNull(resultado);
        assertEquals(101, resultado.getIdVenta());
        assertEquals(new BigDecimal("10000.00"), resultado.getTotal());
        assertEquals(1, resultado.getDetalles().size());

        DetalleVenta detalleGuardado = resultado.getDetalles().get(0);
        assertEquals("Cuaderno 100 Hojas", detalleGuardado.getNombreItem()); // Snapshot
        assertEquals(new BigDecimal("5000.00"), detalleGuardado.getPrecioUnitario()); // Snapshot
        assertEquals(new BigDecimal("10000.00"), detalleGuardado.getSubtotal());

        // Verificación de comunicación con CatalogService para descontar stock
        verify(catalogGateway).descontarStock("PROD-001", 2);

        // Verificación de notificación a AnalyticsService
        verify(analyticsGateway).notificarVenta(resultado);

        // Verificación de registro físico en Kardex
        ArgumentCaptor<MovimientoInventario> movimientoCaptor = ArgumentCaptor.forClass(MovimientoInventario.class);
        verify(movimientoInventarioGateway).save(movimientoCaptor.capture());
        MovimientoInventario movimientoGuardado = movimientoCaptor.getValue();
        assertEquals("PROD-001", movimientoGuardado.getIdProducto());
        assertEquals(1, movimientoGuardado.getIdUsuario());
        assertEquals(2, movimientoGuardado.getCantidad());
        assertEquals(2, movimientoGuardado.getTipoMovimiento().getIdMovimiento());
        assertEquals("Venta #101", movimientoGuardado.getObservacion());
    }

    @Test
    @DisplayName("CU-04 Paso Alterno: Registrar venta combinada de productos y servicios (servicios no mueven Kardex ni stock)")
    void registrarVenta_Exitoso_MixtoProductosYServicios() {
        // Arrange
        Venta venta = new Venta();
        venta.setIdUsuario(2);
        venta.setMetodoPago(new MetodoPago(1, null));

        List<DetalleVenta> items = new ArrayList<>();

        DetalleVenta itemProducto = new DetalleVenta();
        itemProducto.setIdItem("PROD-002");
        itemProducto.setTipoItem(TipoItem.PRODUCTO);
        itemProducto.setCantidad(1);
        items.add(itemProducto);

        DetalleVenta itemServicio = new DetalleVenta();
        itemServicio.setIdItem("1");
        itemServicio.setTipoItem(TipoItem.SERVICIO);
        itemServicio.setCantidad(3);
        itemServicio.setPrecioUnitario(new BigDecimal("3500.00")); // Precio personalizado por vendedor
        items.add(itemServicio);

        venta.setDetalles(items);

        ProductoCatalogo producto = new ProductoCatalogo("PROD-002", "Resma Papel Carta", new BigDecimal("15000.00"), 10, 2);
        ServicioCatalogo servicio = new ServicioCatalogo(1, "Fotocopia B/N", "Impresión simple", new BigDecimal("200.00"));

        when(metodoPagoGateway.findById(1)).thenReturn(Optional.of(metodoPago));
        when(catalogGateway.obtenerProductoPorId("PROD-002")).thenReturn(Optional.of(producto));
        when(catalogGateway.obtenerServicioPorId(1)).thenReturn(Optional.of(servicio));
        when(tipoMovimientoGateway.findByNombre("Salida por Venta")).thenReturn(Optional.of(tipoMovimientoSalida));
        when(ventaGateway.save(any(Venta.class))).thenAnswer(invocation -> {
            Venta v = invocation.getArgument(0);
            v.setIdVenta(202);
            return v;
        });

        // Act
        Venta resultado = registrarVentaUseCase.registrarVenta(venta);

        // Assert
        assertNotNull(resultado);
        // Total = 1 * 15000.00 (producto) + 3 * 3500.00 (servicio) = 15000 + 10500 = 25500.00
        assertEquals(new BigDecimal("25500.00"), resultado.getTotal());

        // Descuento de stock SÓLO para el producto
        verify(catalogGateway, times(1)).descontarStock("PROD-002", 1);

        // Kardex registrado SÓLO para el producto
        verify(movimientoInventarioGateway, times(1)).save(any(MovimientoInventario.class));
    }

    @Test
    @DisplayName("CU-04 Flujo Alternativo 2a: Error cuando el stock del producto es insuficiente")
    void registrarVenta_Fallo_StockInsuficiente() {
        // Arrange
        Venta venta = new Venta();
        venta.setIdUsuario(1);
        venta.setMetodoPago(new MetodoPago(1, null));

        List<DetalleVenta> items = new ArrayList<>();
        DetalleVenta item = new DetalleVenta();
        item.setIdItem("PROD-001");
        item.setTipoItem(TipoItem.PRODUCTO);
        item.setCantidad(15);
        items.add(item);
        venta.setDetalles(items);

        ProductoCatalogo productoCatalogo = new ProductoCatalogo("PROD-001", "Marcador Borrable", new BigDecimal("2000.00"), 5, 2);

        when(metodoPagoGateway.findById(1)).thenReturn(Optional.of(metodoPago));
        when(catalogGateway.obtenerProductoPorId("PROD-001")).thenReturn(Optional.of(productoCatalogo));

        // Act & Assert
        InsufficientStockException exception = assertThrows(InsufficientStockException.class,
                () -> registrarVentaUseCase.registrarVenta(venta));

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(ventaGateway, never()).save(any());
        verify(catalogGateway, never()).descontarStock(anyString(), anyInt());
        verify(movimientoInventarioGateway, never()).save(any());
    }

    @Test
    @DisplayName("Fallo cuando el producto no existe en el catálogo")
    void registrarVenta_Fallo_ProductoNoExisteEnCatalogo() {
        // Arrange
        Venta venta = new Venta();
        venta.setIdUsuario(1);
        venta.setMetodoPago(new MetodoPago(1, null));

        List<DetalleVenta> items = new ArrayList<>();
        DetalleVenta item = new DetalleVenta();
        item.setIdItem("PROD-INEXISTENTE");
        item.setTipoItem(TipoItem.PRODUCTO);
        item.setCantidad(1);
        items.add(item);
        venta.setDetalles(items);

        when(metodoPagoGateway.findById(1)).thenReturn(Optional.of(metodoPago));
        when(catalogGateway.obtenerProductoPorId("PROD-INEXISTENTE")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> registrarVentaUseCase.registrarVenta(venta));

        assertTrue(exception.getMessage().contains("Producto no encontrado"));
        verify(ventaGateway, never()).save(any());
    }

    @Test
    @DisplayName("Fallo cuando la venta no tiene ítems")
    void registrarVenta_Fallo_VentaSinItems() {
        Venta venta = new Venta();
        venta.setIdUsuario(1);
        venta.setMetodoPago(new MetodoPago(1, null));
        venta.setDetalles(new ArrayList<>());

        assertThrows(BusinessRuleException.class, () -> registrarVentaUseCase.registrarVenta(venta));
        verify(ventaGateway, never()).save(any());
    }

    @Test
    @DisplayName("Fallo cuando el método de pago no existe")
    void registrarVenta_Fallo_MetodoPagoNoExiste() {
        Venta venta = new Venta();
        venta.setIdUsuario(1);
        venta.setMetodoPago(new MetodoPago(99, null));

        List<DetalleVenta> items = new ArrayList<>();
        DetalleVenta item = new DetalleVenta();
        item.setIdItem("PROD-001");
        item.setTipoItem(TipoItem.PRODUCTO);
        item.setCantidad(1);
        items.add(item);
        venta.setDetalles(items);

        when(metodoPagoGateway.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> registrarVentaUseCase.registrarVenta(venta));
        verify(ventaGateway, never()).save(any());
    }
}
