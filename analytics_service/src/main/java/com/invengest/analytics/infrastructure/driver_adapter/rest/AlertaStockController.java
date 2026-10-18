package com.invengest.analytics.infrastructure.driver_adapter.rest;

import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response.AlertaStockResponse;
import com.invengest.analytics.infrastructure.mapper.AlertaStockMapper;
import com.invengest.analytics.usecase.ConsultarAlertasStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/alerts")
@RequiredArgsConstructor
public class AlertaStockController {

    private final ConsultarAlertasStockUseCase consultarAlertasStockUseCase;
    private final AlertaStockMapper alertaStockMapper;

    @GetMapping
    public ResponseEntity<List<AlertaStockResponse>> obtenerAlertas(
            @RequestParam(value = "estado", required = false) String estado) {
        List<AlertaStock> alertas = (estado != null && !estado.isBlank())
                ? consultarAlertasStockUseCase.obtenerPorEstado(estado)
                : consultarAlertasStockUseCase.obtenerTodas();
        return ResponseEntity.ok(alertaStockMapper.toResponseList(alertas));
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<AlertaStockResponse>> obtenerAlertasRecientes(
            @RequestParam(value = "limite", required = false, defaultValue = "10") Integer limite) {
        List<AlertaStock> alertas = consultarAlertasStockUseCase.obtenerRecientes(limite);
        return ResponseEntity.ok(alertaStockMapper.toResponseList(alertas));
    }
}
