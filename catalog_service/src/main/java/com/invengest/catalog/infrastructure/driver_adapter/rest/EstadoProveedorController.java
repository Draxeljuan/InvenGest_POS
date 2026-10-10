package com.invengest.catalog.infrastructure.driver_adapter.rest;

import com.invengest.catalog.domain.model.EstadoProveedor;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.EstadoProveedorResponse;
import com.invengest.catalog.infrastructure.mapper.EstadoProveedorRestMapper;
import com.invengest.catalog.usecase.EstadoProveedorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/estados-proveedor")
@RequiredArgsConstructor
public class EstadoProveedorController {

    private final EstadoProveedorUseCase estadoProveedorUseCase;
    private final EstadoProveedorRestMapper estadoProveedorRestMapper;

    @GetMapping
    public ResponseEntity<List<EstadoProveedorResponse>> obtenerTodos() {
        List<EstadoProveedorResponse> responses = estadoProveedorUseCase.obtenerTodos().stream()
                .map(estadoProveedorRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoProveedorResponse> obtenerPorId(@PathVariable Integer id) {
        EstadoProveedor estado = estadoProveedorUseCase.obtenerPorId(id);
        return ResponseEntity.ok(estadoProveedorRestMapper.toResponse(estado));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EstadoProveedorResponse> obtenerPorNombre(@PathVariable String nombre) {
        EstadoProveedor estado = estadoProveedorUseCase.obtenerPorNombre(nombre);
        return ResponseEntity.ok(estadoProveedorRestMapper.toResponse(estado));
    }
}
