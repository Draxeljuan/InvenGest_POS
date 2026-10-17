package com.invengest.transaction.infrastructure.driver_adapter.rest;

import com.invengest.transaction.domain.model.MovimientoInventario;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.MovimientoInventarioRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.MovimientoInventarioResponse;
import com.invengest.transaction.infrastructure.mapper.MovimientoInventarioRestMapper;
import com.invengest.transaction.usecase.MovimientoInventarioUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions/kardex")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioUseCase movimientoInventarioUseCase;
    private final MovimientoInventarioRestMapper mapper;

    @PostMapping("/movimientos")
    public ResponseEntity<MovimientoInventarioResponse> registrarMovimiento(@Valid @RequestBody MovimientoInventarioRequest request) {
        MovimientoInventario movimiento = mapper.toDomain(request);
        MovimientoInventario registrado = movimientoInventarioUseCase.registrarMovimiento(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(registrado));
    }

    @GetMapping("/productos/{idProducto}")
    public ResponseEntity<List<MovimientoInventarioResponse>> obtenerKardexPorProducto(@PathVariable String idProducto) {
        List<MovimientoInventario> movimientos = movimientoInventarioUseCase.obtenerKardexPorProducto(idProducto);
        List<MovimientoInventarioResponse> responses = movimientos.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoInventarioResponse>> listarMovimientos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<MovimientoInventario> movimientos;
        if (inicio != null && fin != null) {
            movimientos = movimientoInventarioUseCase.filtrarPorFechas(inicio, fin);
        } else {
            movimientos = movimientoInventarioUseCase.listarTodos();
        }

        List<MovimientoInventarioResponse> responses = movimientos.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
