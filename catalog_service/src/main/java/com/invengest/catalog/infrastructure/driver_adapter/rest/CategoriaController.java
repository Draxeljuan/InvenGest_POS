package com.invengest.catalog.infrastructure.driver_adapter.rest;

import com.invengest.catalog.domain.model.Categoria;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.CategoriaRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.CategoriaResponse;
import com.invengest.catalog.infrastructure.mapper.CategoriaRestMapper;
import com.invengest.catalog.usecase.CategoriaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/catalog/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaUseCase categoriaUseCase;
    private final CategoriaRestMapper categoriaRestMapper;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        List<CategoriaResponse> responses = categoriaUseCase.obtenerTodas().stream()
                .map(categoriaRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaUseCase.obtenerPorId(id);
        return ResponseEntity.ok(categoriaRestMapper.toResponse(categoria));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        Categoria categoria = categoriaRestMapper.toDomain(request);
        Categoria creada = categoriaUseCase.crear(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRestMapper.toResponse(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(@PathVariable Integer id, @Valid @RequestBody CategoriaRequest request) {
        Categoria categoria = categoriaRestMapper.toDomain(request);
        categoria.setIdCategoria(id);
        Categoria actualizada = categoriaUseCase.actualizar(categoria);
        return ResponseEntity.ok(categoriaRestMapper.toResponse(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaUseCase.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}