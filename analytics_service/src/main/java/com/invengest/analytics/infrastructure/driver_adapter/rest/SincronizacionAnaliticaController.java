package com.invengest.analytics.infrastructure.driver_adapter.rest;

import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.domain.model.ItemVentaSincronizacion;
import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.request.SincronizarAlertaStockRequest;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.request.SincronizarVentaRequest;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response.AlertaStockResponse;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response.ResumenVentaResponse;
import com.invengest.analytics.infrastructure.mapper.AlertaStockMapper;
import com.invengest.analytics.infrastructure.mapper.ResumenVentaMapper;
import com.invengest.analytics.usecase.SincronizarAnaliticaUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/analytics/internal")
@RequiredArgsConstructor
public class SincronizacionAnaliticaController {

    private final SincronizarAnaliticaUseCase sincronizarAnaliticaUseCase;
    private final ResumenVentaMapper resumenVentaMapper;
    private final AlertaStockMapper alertaStockMapper;

    @PostMapping("/ventas")
    public ResponseEntity<ResumenVentaResponse> sincronizarVenta(
            @Valid @RequestBody SincronizarVentaRequest request) {
        List<ItemVentaSincronizacion> items = request.getItems() != null
                ? request.getItems().stream()
                        .map(item -> ItemVentaSincronizacion.builder()
                                .idProducto(item.getIdProducto())
                                .nombreProducto(item.getNombreProducto())
                                .cantidadVendida(item.getCantidadVendida())
                                .build())
                        .toList()
                : Collections.emptyList();

        ResumenVentaDiaria resumenActualizado = sincronizarAnaliticaUseCase.registrarVentaSincronizada(
                request.getFecha(),
                request.getTotalVenta(),
                items
        );

        return ResponseEntity.ok(resumenVentaMapper.toResponse(resumenActualizado));
    }

    @PostMapping("/alertas-stock")
    public ResponseEntity<AlertaStockResponse> sincronizarAlertaStock(
            @Valid @RequestBody SincronizarAlertaStockRequest request) {
        Optional<AlertaStock> alerta = sincronizarAnaliticaUseCase.sincronizarAlertaStock(
                request.getIdProducto(),
                request.getNombreProducto(),
                request.getStockActual()
        );

        return alerta.map(a -> ResponseEntity.ok(alertaStockMapper.toResponse(a)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
