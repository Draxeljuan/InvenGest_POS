package com.invengest.transaction.usecase;

import com.invengest.transaction.domain.exception.BusinessRuleException;
import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.gateway.MovimientoInventarioGateway;
import com.invengest.transaction.domain.gateway.TipoMovimientoGateway;
import com.invengest.transaction.domain.model.MovimientoInventario;
import com.invengest.transaction.domain.model.TipoMovimiento;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MovimientoInventarioUseCase {

    private final MovimientoInventarioGateway movimientoInventarioGateway;
    private final TipoMovimientoGateway tipoMovimientoGateway;

    public MovimientoInventario registrarMovimiento(MovimientoInventario movimiento) {
        if (movimiento == null) {
            throw new BusinessRuleException("El movimiento de inventario no puede ser nulo");
        }
        if (movimiento.getIdProducto() == null || movimiento.getIdProducto().isBlank()) {
            throw new BusinessRuleException("El ID del producto es obligatorio");
        }
        if (movimiento.getIdUsuario() == null) {
            throw new BusinessRuleException("El ID del usuario es obligatorio");
        }
        if (movimiento.getCantidad() == null || movimiento.getCantidad() <= 0) {
            throw new BusinessRuleException("La cantidad debe ser mayor a 0");
        }
        if (movimiento.getTipoMovimiento() == null || movimiento.getTipoMovimiento().getIdMovimiento() == null) {
            throw new BusinessRuleException("El tipo de movimiento es obligatorio");
        }

        TipoMovimiento tipoMovimiento = tipoMovimientoGateway.findById(movimiento.getTipoMovimiento().getIdMovimiento())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de movimiento no encontrado con ID: " + movimiento.getTipoMovimiento().getIdMovimiento()));
        movimiento.setTipoMovimiento(tipoMovimiento);

        if (movimiento.getFechaMovimiento() == null) {
            movimiento.setFechaMovimiento(LocalDateTime.now());
        }

        return movimientoInventarioGateway.save(movimiento);
    }

    public List<MovimientoInventario> obtenerKardexPorProducto(String idProducto) {
        if (idProducto == null || idProducto.isBlank()) {
            throw new BusinessRuleException("El ID del producto es obligatorio para consultar el Kardex");
        }
        return movimientoInventarioGateway.findByIdProducto(idProducto);
    }

    public List<MovimientoInventario> listarTodos() {
        return movimientoInventarioGateway.findAll();
    }

    public List<MovimientoInventario> filtrarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoInventarioGateway.findByFechaBetween(inicio, fin);
    }
}
