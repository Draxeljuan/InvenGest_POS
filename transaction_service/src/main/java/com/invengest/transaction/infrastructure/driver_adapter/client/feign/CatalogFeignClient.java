package com.invengest.transaction.infrastructure.driver_adapter.client.feign;

import com.invengest.transaction.infrastructure.driver_adapter.client.dto.CatalogProductoDto;
import com.invengest.transaction.infrastructure.driver_adapter.client.dto.CatalogServicioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "catalog-service", url = "${catalog.service.url}")
public interface CatalogFeignClient {

    @GetMapping("/api/catalog/productos/{id}")
    CatalogProductoDto obtenerProductoPorId(@PathVariable("id") String id);

    @PutMapping("/api/catalog/productos/{id}/descontar-stock")
    CatalogProductoDto descontarStock(@PathVariable("id") String id, @RequestParam("cantidad") Integer cantidad);

    @GetMapping("/api/catalog/servicios/{id}")
    CatalogServicioDto obtenerServicioPorId(@PathVariable("id") Integer id);
}
