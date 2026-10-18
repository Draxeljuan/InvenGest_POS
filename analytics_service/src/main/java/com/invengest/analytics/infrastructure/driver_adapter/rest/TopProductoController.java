package com.invengest.analytics.infrastructure.driver_adapter.rest;

import com.invengest.analytics.domain.model.TopProducto;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response.TopProductoResponse;
import com.invengest.analytics.infrastructure.mapper.TopProductoMapper;
import com.invengest.analytics.usecase.ConsultarTopProductosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics/top-products")
@RequiredArgsConstructor
public class TopProductoController {

    private final ConsultarTopProductosUseCase consultarTopProductosUseCase;
    private final TopProductoMapper topProductoMapper;

    @GetMapping
    public ResponseEntity<List<TopProductoResponse>> obtenerPorFecha(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<TopProducto> productos = consultarTopProductosUseCase.obtenerPorFecha(fecha);
        return ResponseEntity.ok(topProductoMapper.toResponseList(productos));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<TopProductoResponse>> obtenerPorRango(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(value = "limite", required = false, defaultValue = "10") Integer limite) {
        List<TopProducto> productos = consultarTopProductosUseCase.obtenerTopPorRango(inicio, fin, limite);
        return ResponseEntity.ok(topProductoMapper.toResponseList(productos));
    }
}
