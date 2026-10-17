package com.invengest.transaction.infrastructure.driver_adapter.client.adapter;

import com.invengest.transaction.domain.model.ProductoCatalogo;
import com.invengest.transaction.domain.model.ServicioCatalogo;
import com.invengest.transaction.infrastructure.driver_adapter.client.dto.CatalogProductoDto;
import com.invengest.transaction.infrastructure.driver_adapter.client.dto.CatalogServicioDto;
import com.invengest.transaction.infrastructure.driver_adapter.client.feign.CatalogFeignClient;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogAdapterTest {

    @Mock
    private CatalogFeignClient feignClient;

    @InjectMocks
    private CatalogAdapter adapter;

    @Test
    @DisplayName("Obtener producto existente desde OpenFeign")
    void obtenerProductoPorId_Exitoso() {
        CatalogProductoDto dto = new CatalogProductoDto("PROD-01", "Lapiz", new BigDecimal("1200.00"), 50, 10);
        when(feignClient.obtenerProductoPorId("PROD-01")).thenReturn(dto);

        Optional<ProductoCatalogo> opt = adapter.obtenerProductoPorId("PROD-01");

        assertTrue(opt.isPresent());
        assertEquals("PROD-01", opt.get().getIdProducto());
        assertEquals("Lapiz", opt.get().getNombre());
        assertEquals(50, opt.get().getStock());
    }

    @Test
    @DisplayName("Obtener producto inexistente (FeignException.NotFound) retorna Optional.empty()")
    void obtenerProductoPorId_NotFound() {
        Request request = Request.create(Request.HttpMethod.GET, "/api/catalog/productos/99", Collections.emptyMap(), null, new RequestTemplate());
        when(feignClient.obtenerProductoPorId("99")).thenThrow(new FeignException.NotFound("Not found", request, null, null));

        Optional<ProductoCatalogo> opt = adapter.obtenerProductoPorId("99");

        assertTrue(opt.isEmpty());
    }

    @Test
    @DisplayName("Obtener servicio existente desde OpenFeign")
    void obtenerServicioPorId_Exitoso() {
        CatalogServicioDto dto = new CatalogServicioDto(1, "Anillado", "Anillado espiral", new BigDecimal("4500.00"));
        when(feignClient.obtenerServicioPorId(1)).thenReturn(dto);

        Optional<ServicioCatalogo> opt = adapter.obtenerServicioPorId(1);

        assertTrue(opt.isPresent());
        assertEquals("Anillado", opt.get().getNombre());
    }

    @Test
    @DisplayName("Descontar stock invoca cliente OpenFeign")
    void descontarStock_Exitoso() {
        adapter.descontarStock("PROD-01", 5);
        verify(feignClient).descontarStock("PROD-01", 5);
    }
}
