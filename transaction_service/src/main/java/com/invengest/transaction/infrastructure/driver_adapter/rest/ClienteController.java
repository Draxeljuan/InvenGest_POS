package com.invengest.transaction.infrastructure.driver_adapter.rest;

import com.invengest.transaction.domain.model.Cliente;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.ClienteRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.ClienteResponse;
import com.invengest.transaction.infrastructure.mapper.ClienteRestMapper;
import com.invengest.transaction.usecase.ClienteUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteUseCase clienteUseCase;
    private final ClienteRestMapper mapper;

    @PostMapping
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody ClienteRequest request) {
        Cliente cliente = mapper.toDomain(request);
        Cliente registrado = clienteUseCase.registrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(registrado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Integer id) {
        Cliente cliente = clienteUseCase.obtenerPorId(id);
        return ResponseEntity.ok(mapper.toResponse(cliente));
    }

    @GetMapping("/documento/{documento}")
    public ResponseEntity<ClienteResponse> obtenerPorDocumento(@PathVariable String documento) {
        Cliente cliente = clienteUseCase.obtenerPorDocumento(documento);
        return ResponseEntity.ok(mapper.toResponse(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        List<ClienteResponse> responses = clienteUseCase.listarTodos().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
