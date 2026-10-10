package com.invengest.catalog.infrastructure.driver_adapter.rest;

import com.invengest.catalog.domain.model.CategoriaServicio;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.CategoriaServicioRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.CategoriaServicioResponse;
import com.invengest.catalog.infrastructure.mapper.CategoriaServicioRestMapper;
import com.invengest.catalog.usecase.CategoriaServicioUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/categorias-servicio")
@RequiredArgsConstructor
public class CategoriaServicioController {

    private final CategoriaServicioUseCase categoriaServicioUseCase;
    private final CategoriaServicioRestMapper categoriaServicioRestMapper;

    @GetMapping
    public ResponseEntity<List<CategoriaServicioResponse>> listarTodas() {
        List<CategoriaServicioResponse> responses = categoriaServicioUseCase.obtenerTodas().stream()
                .map(categoriaServicioRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaServicioResponse> obtenerPorId(@PathVariable Integer id) {
        CategoriaServicio categoriaServicio = categoriaServicioUseCase.obtenerPorId(id);
        return ResponseEntity.ok(categoriaServicioRestMapper.toResponse(categoriaServicio));
    }

    @PostMapping
    public ResponseEntity<CategoriaServicioResponse> crear(@Valid @RequestBody CategoriaServicioRequest request) {
        CategoriaServicio categoriaServicio = categoriaServicioRestMapper.toDomain(request);
        CategoriaServicio creada = categoriaServicioUseCase.crear(categoriaServicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaServicioRestMapper.toResponse(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaServicioResponse> actualizar(@PathVariable Integer id, @Valid @RequestBody CategoriaServicioRequest request) {
        CategoriaServicio categoriaServicio = categoriaServicioRestMapper.toDomain(request);
        categoriaServicio.setIdCategoriaServicio(id);
        CategoriaServicio actualizada = categoriaServicioUseCase.actualizar(categoriaServicio);
        return ResponseEntity.ok(categoriaServicioRestMapper.toResponse(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaServicioUseCase.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
