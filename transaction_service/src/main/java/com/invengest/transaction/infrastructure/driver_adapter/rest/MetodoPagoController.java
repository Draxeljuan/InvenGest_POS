package com.invengest.transaction.infrastructure.driver_adapter.rest;

import com.invengest.transaction.domain.model.MetodoPago;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.MetodoPagoResponse;
import com.invengest.transaction.infrastructure.mapper.MetodoPagoRestMapper;
import com.invengest.transaction.usecase.MetodoPagoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/transactions/metodos-pago", "/api/transaction/metodos-pago"})
@RequiredArgsConstructor
public class MetodoPagoController {

    private final MetodoPagoUseCase metodoPagoUseCase;
    private final MetodoPagoRestMapper mapper;

    @GetMapping
    public ResponseEntity<List<MetodoPagoResponse>> listarTodos() {
        List<MetodoPagoResponse> responses = metodoPagoUseCase.listarTodos().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponse> obtenerPorId(@PathVariable Integer id) {
        MetodoPago metodoPago = metodoPagoUseCase.obtenerPorId(id);
        return ResponseEntity.ok(mapper.toResponse(metodoPago));
    }
}
