package com.invengest.transaction.infrastructure.driver_adapter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "analytics-service", url = "${analytics.service.url:http://localhost:8084}")
public interface AnalyticsFeignClient {

    @PostMapping("/api/analytics/internal/ventas")
    void sincronizarVenta(@RequestBody SincronizarVentaRequestDto request);
}
