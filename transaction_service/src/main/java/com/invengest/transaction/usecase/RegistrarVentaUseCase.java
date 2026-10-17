package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.BusinessRuleException;
import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.exception.InsufficientStockException;
import com.invengest.transaction.domain.gateway.*;
import com.invengest.transaction.domain.model.*;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RegistrarVentaUseCase {

    private final VentaGateway ventaGateway;
    private final CatalogGateway catalogGateway;
    private final MovimientoInventarioGateway movimientoInventarioGateway;
    private final ClienteGateway clienteGateway;
    private final MetodoPagoGateway metodoPagoGateway;
    private final TipoMovimientoGateway tipoMovimientoGateway;

    private static final int ID_TIPO_MOVIMIENTO_SALIDA_VENTA = 2;
    private static final String NOMBRE_MOVIMIENTO_SALIDA_VENTA = "Salida por Venta";

    public Venta registrarVenta(Venta venta) {
        validarCabeceraVenta(venta);
        validarClienteYMetodoPago(venta);

        List<DetalleVenta> detallesProcesados = procesarDetalles(venta.getDetalles());
        venta.setDetalles(detallesProcesados);

        BigDecimal total = detallesProcesados.stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        venta.setTotal(total);

        if (venta.getFecha() == null) {
            venta.setFecha(LocalDateTime.now());
        }

        Venta ventaGuardada = ventaGateway.save(venta);

        procesarActualizacionStockYKardex(ventaGuardada, detallesProcesados);

        return ventaGuardada;
    }

    private void validarCabeceraVenta(Venta venta) {
        if (venta == null) {
            throw new BusinessRuleException("Los datos de la venta no pueden ser nulos");
        }
        if (venta.getIdUsuario() == null) {
            throw new BusinessRuleException("El ID del usuario vendedor es obligatorio");
        }
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new BusinessRuleException("La venta debe contener al menos un ítem");
        }
    }

    private void validarClienteYMetodoPago(Venta venta) {
        if (venta.getMetodoPago() == null || venta.getMetodoPago().getIdMetodoPago() == null) {
            throw new BusinessRuleException("El método de pago es obligatorio");
        }
        MetodoPago metodoPago = metodoPagoGateway.findById(venta.getMetodoPago().getIdMetodoPago())
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado con ID: " + venta.getMetodoPago().getIdMetodoPago()));
        venta.setMetodoPago(metodoPago);

        if (venta.getCliente() != null && venta.getCliente().getIdCliente() != null) {
            Cliente cliente = clienteGateway.findById(venta.getCliente().getIdCliente())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + venta.getCliente().getIdCliente()));
            venta.setCliente(cliente);
        }
    }

    private List<DetalleVenta> procesarDetalles(List<DetalleVenta> detalles) {
        List<DetalleVenta> procesados = new ArrayList<>();

        for (DetalleVenta detalle : detalles) {
            if (detalle.getIdItem() == null || detalle.getIdItem().isBlank()) {
                throw new BusinessRuleException("El identificador del ítem es obligatorio");
            }
            if (detalle.getTipoItem() == null) {
                throw new BusinessRuleException("El tipo de ítem (PRODUCTO o SERVICIO) es obligatorio");
            }
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new BusinessRuleException("La cantidad debe ser mayor a 0 para el ítem: " + detalle.getIdItem());
            }

            if (detalle.getTipoItem() == TipoItem.PRODUCTO) {
                procesarItemProducto(detalle);
            } else if (detalle.getTipoItem() == TipoItem.SERVICIO) {
                procesarItemServicio(detalle);
            }

            procesados.add(detalle);
        }

        return procesados;
    }

    private void procesarItemProducto(DetalleVenta detalle) {
        ProductoCatalogo producto = catalogGateway.obtenerProductoPorId(detalle.getIdItem())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado en catálogo con código: " + detalle.getIdItem()));

        if (producto.getStock() == null || producto.getStock() < detalle.getCantidad()) {
            int stockActual = producto.getStock() != null ? producto.getStock() : 0;
            throw new InsufficientStockException("Stock insuficiente para el producto: " + producto.getNombre()
                    + " (Disponible: " + stockActual + ", Solicitado: " + detalle.getCantidad() + ")");
        }

        // Snapshot histórico del producto al momento exacto de la venta
        detalle.setNombreItem(producto.getNombre());
        BigDecimal precioUnitario = detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario() : producto.getPrecioVenta();
        if (precioUnitario == null) {
            precioUnitario = BigDecimal.ZERO;
        }
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(precioUnitario.multiply(BigDecimal.valueOf(detalle.getCantidad())));
    }

    private void procesarItemServicio(DetalleVenta detalle) {
        Integer idServicio = null;
        try {
            idServicio = Integer.valueOf(detalle.getIdItem());
        } catch (NumberFormatException ignored) {
            // El ID del servicio puede ser una clave alfanumérica si se define así en el frontend
        }

        ServicioCatalogo servicio = null;
        if (idServicio != null) {
            servicio = catalogGateway.obtenerServicioPorId(idServicio).orElse(null);
        }

        if (servicio != null) {
            if (detalle.getNombreItem() == null || detalle.getNombreItem().isBlank()) {
                detalle.setNombreItem(servicio.getNombre());
            }
            if (detalle.getPrecioUnitario() == null) {
                detalle.setPrecioUnitario(servicio.getPrecioSugerido());
            }
        }

        if (detalle.getNombreItem() == null || detalle.getNombreItem().isBlank()) {
            throw new BusinessRuleException("El nombre del servicio es obligatorio para el ítem: " + detalle.getIdItem());
        }
        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("El precio unitario del servicio no puede ser nulo o negativo");
        }

        detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
    }

    private void procesarActualizacionStockYKardex(Venta venta, List<DetalleVenta> detalles) {
        TipoMovimiento tipoMovimientoSalida = obtenerTipoMovimientoSalida();

        for (DetalleVenta detalle : detalles) {
            if (detalle.getTipoItem() == TipoItem.PRODUCTO) {
                // 1. Notificar a catalog_service el descuento de stock
                catalogGateway.descontarStock(detalle.getIdItem(), detalle.getCantidad());

                // 2. Registrar salida física en Kardex (movimiento_inventario)
                MovimientoInventario movimiento = new MovimientoInventario();
                movimiento.setIdProducto(detalle.getIdItem());
                movimiento.setIdUsuario(venta.getIdUsuario());
                movimiento.setTipoMovimiento(tipoMovimientoSalida);
                movimiento.setCantidad(detalle.getCantidad());
                movimiento.setFechaMovimiento(LocalDateTime.now());
                movimiento.setObservacion("Venta #" + venta.getIdVenta());

                movimientoInventarioGateway.save(movimiento);
            }
            // Regla CU-04: Si es SERVICIO no se descuenta stock ni se registra en movimiento_inventario
        }
    }

    private TipoMovimiento obtenerTipoMovimientoSalida() {
        return tipoMovimientoGateway.findByNombre(NOMBRE_MOVIMIENTO_SALIDA_VENTA)
                .or(() -> tipoMovimientoGateway.findById(ID_TIPO_MOVIMIENTO_SALIDA_VENTA))
                .orElseGet(() -> new TipoMovimiento(ID_TIPO_MOVIMIENTO_SALIDA_VENTA, NOMBRE_MOVIMIENTO_SALIDA_VENTA));
    }
}
