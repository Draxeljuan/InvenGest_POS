package com.invengest.analytics.usecase;

import com.invengest.analytics.domain.exception.BusinessRuleException;
import com.invengest.analytics.domain.gateway.AlertaStockGateway;
import com.invengest.analytics.domain.gateway.ResumenVentaGateway;
import com.invengest.analytics.domain.gateway.TopProductoGateway;
import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.ItemVentaSincronizacion;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.domain.model.TopProducto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SincronizarAnaliticaUseCase {

    private static final short UMBRAL_BAJO_STOCK = 10;
    private static final String ESTADO_AGOTADO = "Agotado";
    private static final String ESTADO_BAJO_STOCK = "Bajo Stock";

    private final ResumenVentaGateway resumenVentaGateway;
    private final TopProductoGateway topProductoGateway;
    private final AlertaStockGateway alertaStockGateway;

    public SincronizarAnaliticaUseCase(
            ResumenVentaGateway resumenVentaGateway,
            TopProductoGateway topProductoGateway,
            AlertaStockGateway alertaStockGateway) {
        this.resumenVentaGateway = resumenVentaGateway;
        this.topProductoGateway = topProductoGateway;
        this.alertaStockGateway = alertaStockGateway;
    }

    public ResumenVentaDiaria registrarVentaSincronizada(
            LocalDate fecha,
            BigDecimal totalVenta,
            List<ItemVentaSincronizacion> items) {
        if (totalVenta == null || totalVenta.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("El monto total de la venta debe ser mayor o igual a cero.");
        }

        LocalDate fechaOperacion = fecha != null ? fecha : LocalDate.now();

        ResumenVentaDiaria resumen = resumenVentaGateway.findByFecha(fechaOperacion)
                .orElseGet(() -> ResumenVentaDiaria.builder()
                        .fecha(fechaOperacion)
                        .totalVentas(BigDecimal.ZERO)
                        .cantidadOperaciones(0)
                        .build());

        BigDecimal nuevoTotal = (resumen.getTotalVentas() != null ? resumen.getTotalVentas() : BigDecimal.ZERO)
                .add(totalVenta);
        int nuevasOperaciones = (resumen.getCantidadOperaciones() != null ? resumen.getCantidadOperaciones() : 0) + 1;

        resumen.setTotalVentas(nuevoTotal);
        resumen.setCantidadOperaciones(nuevasOperaciones);

        ResumenVentaDiaria resumenGuardado = resumenVentaGateway.save(resumen);

        if (items != null) {
            for (ItemVentaSincronizacion item : items) {
                if (item.getIdProducto() != null && !item.getIdProducto().isBlank()
                        && item.getCantidadVendida() != null && item.getCantidadVendida() > 0) {
                    TopProducto topProducto = topProductoGateway
                            .findByIdProductoAndFecha(item.getIdProducto(), fechaOperacion)
                            .orElseGet(() -> TopProducto.builder()
                                    .idProducto(item.getIdProducto())
                                    .nombreProducto(item.getNombreProducto())
                                    .cantidadVendida(0)
                                    .fecha(fechaOperacion)
                                    .build());

                    int cantidadAcumulada = (topProducto.getCantidadVendida() != null ? topProducto.getCantidadVendida() : 0)
                            + item.getCantidadVendida();
                    topProducto.setCantidadVendida(cantidadAcumulada);
                    if (item.getNombreProducto() != null && !item.getNombreProducto().isBlank()) {
                        topProducto.setNombreProducto(item.getNombreProducto());
                    }

                    topProductoGateway.save(topProducto);
                }
            }
        }

        return resumenGuardado;
    }

    public Optional<AlertaStock> sincronizarAlertaStock(
            String idProducto,
            String nombreProducto,
            Short stockActual) {
        if (idProducto == null || idProducto.isBlank()) {
            throw new BusinessRuleException("El identificador del producto es obligatorio.");
        }
        if (nombreProducto == null || nombreProducto.isBlank()) {
            throw new BusinessRuleException("El nombre del producto es obligatorio.");
        }
        if (stockActual == null) {
            throw new BusinessRuleException("El stock actual del producto es obligatorio.");
        }

        // Si el stock se normalizó (>= 10), eliminamos cualquier alerta previa del producto
        if (stockActual >= UMBRAL_BAJO_STOCK) {
            alertaStockGateway.deleteByIdProducto(idProducto);
            return Optional.empty();
        }

        String nuevoEstado = stockActual <= 0 ? ESTADO_AGOTADO : ESTADO_BAJO_STOCK;

        AlertaStock alerta = alertaStockGateway.findByIdProducto(idProducto)
                .orElseGet(() -> AlertaStock.builder()
                        .idProducto(idProducto)
                        .build());

        alerta.setNombreProducto(nombreProducto);
        alerta.setStockActual(stockActual);
        alerta.setEstado(nuevoEstado);
        alerta.setFechaAlerta(LocalDateTime.now());

        return Optional.of(alertaStockGateway.save(alerta));
    }
}
