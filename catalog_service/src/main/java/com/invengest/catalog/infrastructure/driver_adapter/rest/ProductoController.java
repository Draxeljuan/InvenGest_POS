package com.invengest.catalog.infrastructure.driver_adapter.rest;

import com.invengest.catalog.domain.model.Producto;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.ProductoRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.ProductoResponse;
import com.invengest.catalog.infrastructure.mapper.ProductoRestMapper;
import com.invengest.catalog.usecase.ProductoUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;
    private final ProductoRestMapper productoRestMapper;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        List<ProductoResponse> responses = productoUseCase.obtenerTodos().stream()
                .map(productoRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable String id) {
        Producto producto = productoUseCase.obtenerPorId(id);
        return ResponseEntity.ok(productoRestMapper.toResponse(producto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponse>> buscarPorCriterio(@RequestParam String query) {
        List<ProductoResponse> responses = productoUseCase.buscarPorCriterio(query).stream()
                .map(productoRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoResponse>> obtenerStockBajo() {
        List<ProductoResponse> responses = productoUseCase.obtenerProductosStockBajo().stream()
                .map(productoRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> registrar(@Valid @RequestBody ProductoRequest request) {
        Producto producto = productoRestMapper.toDomain(request);
        Producto registrado = productoUseCase.registrar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoRestMapper.toResponse(registrado));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable String id, @Valid @RequestBody ProductoRequest request) {
        Producto producto = productoRestMapper.toDomain(request);
        producto.setIdProducto(id);
        Producto actualizado = productoUseCase.actualizar(producto);
        return ResponseEntity.ok(productoRestMapper.toResponse(actualizado));
    }

    @PutMapping("/{id}/descontar-stock")
    public ResponseEntity<ProductoResponse> descontarStock(@PathVariable String id, @RequestParam Integer cantidad) {
        Producto actualizado = productoUseCase.descontarStock(id, cantidad);
        return ResponseEntity.ok(productoRestMapper.toResponse(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        productoUseCase.eliminacionLogica(id);
        return ResponseEntity.noContent().build();
    }
}