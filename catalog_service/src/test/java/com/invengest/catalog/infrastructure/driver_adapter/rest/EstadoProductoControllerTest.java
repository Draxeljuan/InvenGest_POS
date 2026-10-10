package com.invengest.catalog.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.model.EstadoProducto;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.EstadoProductoResponse;
import com.invengest.catalog.infrastructure.mapper.EstadoProductoRestMapper;
import com.invengest.catalog.usecase.EstadoProductoUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadoProductoController.class)
class EstadoProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EstadoProductoUseCase estadoProductoUseCase;

    @MockitoBean
    private EstadoProductoRestMapper estadoProductoRestMapper;

    @Test
    void obtenerTodos_Success_200() throws Exception {
        EstadoProducto domain = new EstadoProducto();
        EstadoProductoResponse response = new EstadoProductoResponse(1, "ACTIVO");

        when(estadoProductoUseCase.obtenerTodos()).thenReturn(List.of(domain));
        when(estadoProductoRestMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/api/catalog/estados-producto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"));
    }

    @Test
    void obtenerPorId_Success_200() throws Exception {
        EstadoProducto domain = new EstadoProducto();
        EstadoProductoResponse response = new EstadoProductoResponse(1, "ACTIVO");

        when(estadoProductoUseCase.obtenerPorId(1)).thenReturn(domain);
        when(estadoProductoRestMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/api/catalog/estados-producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstado").value(1));
    }

    @Test
    void obtenerPorId_NotFound_404() throws Exception {
        when(estadoProductoUseCase.obtenerPorId(99)).thenThrow(new EntityNotFoundException("No encontrado"));

        mockMvc.perform(get("/api/catalog/estados-producto/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No encontrado"));
    }

    @Test
    void obtenerPorNombre_Success_200() throws Exception {
        EstadoProducto domain = new EstadoProducto();
        EstadoProductoResponse response = new EstadoProductoResponse(1, "ACTIVO");

        when(estadoProductoUseCase.obtenerPorNombre("ACTIVO")).thenReturn(domain);
        when(estadoProductoRestMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/api/catalog/estados-producto/nombre/ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ACTIVO"));
    }

    @Test
    void obtenerPorNombre_NotFound_404() throws Exception {
        when(estadoProductoUseCase.obtenerPorNombre(anyString())).thenThrow(new EntityNotFoundException("No encontrado"));

        mockMvc.perform(get("/api/catalog/estados-producto/nombre/INACTIVO"))
                .andExpect(status().isNotFound());
    }
}
