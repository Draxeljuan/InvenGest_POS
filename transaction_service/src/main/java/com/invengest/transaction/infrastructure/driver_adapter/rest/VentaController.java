package com.invengest.transaction.infrastructure.driver_adapter.rest;

import com.invengest.transaction.domain.model.Venta;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.RegistrarVentaRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.VentaResponse;
import com.invengest.transaction.infrastructure.mapper.VentaRestMapper;
import com.invengest.transaction.usecase.ConsultarVentaUseCase;
import com.invengest.transaction.usecase.RegistrarVentaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final RegistrarVentaUseCase registrarVentaUseCase;
    private final ConsultarVentaUseCase consultarVentaUseCase;
    private final VentaRestMapper ventaRestMapper;

    @PostMapping
    public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody RegistrarVentaRequest request) {
        Venta venta = ventaRestMapper.toDomain(request);
        Venta ventaRegistrada = registrarVentaUseCase.registrarVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaRestMapper.toResponse(ventaRegistrada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerPorId(@PathVariable Integer id) {
        Venta venta = consultarVentaUseCase.obtenerPorId(id);
        return ResponseEntity.ok(ventaRestMapper.toResponse(venta));
    }

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @RequestParam(required = false) Integer idUsuario) {

        List<Venta> ventas;
        if (inicio != null && fin != null) {
            ventas = consultarVentaUseCase.filtrarPorFechas(inicio, fin);
        } else if (idUsuario != null) {
            ventas = consultarVentaUseCase.listarPorUsuario(idUsuario);
        } else {
            ventas = consultarVentaUseCase.listarTodas();
        }

        List<VentaResponse> responses = ventas.stream()
                .map(ventaRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
