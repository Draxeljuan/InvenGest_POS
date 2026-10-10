package com.invengest.catalog.infrastructure.driver_adapter.rest;

import com.invengest.catalog.domain.model.EstadoProducto;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.EstadoProductoResponse;
import com.invengest.catalog.infrastructure.mapper.EstadoProductoRestMapper;
import com.invengest.catalog.usecase.EstadoProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/estados-producto")
@RequiredArgsConstructor
public class EstadoProductoController {

    private final EstadoProductoUseCase estadoProductoUseCase;
    private final EstadoProductoRestMapper estadoProductoRestMapper;

    @GetMapping
    public ResponseEntity<List<EstadoProductoResponse>> obtenerTodos() {
        List<EstadoProductoResponse> responses = estadoProductoUseCase.obtenerTodos().stream()
                .map(estadoProductoRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoProductoResponse> obtenerPorId(@PathVariable Integer id) {
        EstadoProducto estado = estadoProductoUseCase.obtenerPorId(id);
        return ResponseEntity.ok(estadoProductoRestMapper.toResponse(estado));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EstadoProductoResponse> obtenerPorNombre(@PathVariable String nombre) {
        EstadoProducto estado = estadoProductoUseCase.obtenerPorNombre(nombre);
        return ResponseEntity.ok(estadoProductoRestMapper.toResponse(estado));
    }
}
