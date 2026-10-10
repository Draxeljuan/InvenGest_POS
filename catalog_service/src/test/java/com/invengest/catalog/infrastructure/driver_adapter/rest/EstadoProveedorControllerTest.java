package com.invengest.catalog.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.model.EstadoProveedor;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.EstadoProveedorResponse;
import com.invengest.catalog.infrastructure.mapper.EstadoProveedorRestMapper;
import com.invengest.catalog.usecase.EstadoProveedorUseCase;
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

@WebMvcTest(EstadoProveedorController.class)
class EstadoProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EstadoProveedorUseCase estadoProveedorUseCase;

    @MockitoBean
    private EstadoProveedorRestMapper estadoProveedorRestMapper;

    @Test
    void obtenerTodos_Success_200() throws Exception {
        EstadoProveedor domain = new EstadoProveedor();
        EstadoProveedorResponse response = new EstadoProveedorResponse(1, "ACTIVO");

        when(estadoProveedorUseCase.obtenerTodos()).thenReturn(List.of(domain));
        when(estadoProveedorRestMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/api/catalog/estados-proveedor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"));
    }

    @Test
    void obtenerPorId_Success_200() throws Exception {
        EstadoProveedor domain = new EstadoProveedor();
        EstadoProveedorResponse response = new EstadoProveedorResponse(1, "ACTIVO");

        when(estadoProveedorUseCase.obtenerPorId(1)).thenReturn(domain);
        when(estadoProveedorRestMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/api/catalog/estados-proveedor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstadoProveedor").value(1));
    }

    @Test
    void obtenerPorId_NotFound_404() throws Exception {
        when(estadoProveedorUseCase.obtenerPorId(99)).thenThrow(new EntityNotFoundException("No encontrado"));

        mockMvc.perform(get("/api/catalog/estados-proveedor/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No encontrado"));
    }

    @Test
    void obtenerPorNombre_Success_200() throws Exception {
        EstadoProveedor domain = new EstadoProveedor();
        EstadoProveedorResponse response = new EstadoProveedorResponse(1, "ACTIVO");

        when(estadoProveedorUseCase.obtenerPorNombre("ACTIVO")).thenReturn(domain);
        when(estadoProveedorRestMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/api/catalog/estados-proveedor/nombre/ACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ACTIVO"));
    }

    @Test
    void obtenerPorNombre_NotFound_404() throws Exception {
        when(estadoProveedorUseCase.obtenerPorNombre(anyString())).thenThrow(new EntityNotFoundException("No encontrado"));

        mockMvc.perform(get("/api/catalog/estados-proveedor/nombre/INACTIVO"))
                .andExpect(status().isNotFound());
    }
}
