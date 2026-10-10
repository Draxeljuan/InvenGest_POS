package com.invengest.catalog.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.model.Proveedor;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.ProveedorRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.ProveedorResponse;
import com.invengest.catalog.infrastructure.mapper.ProveedorRestMapper;
import com.invengest.catalog.usecase.ProveedorUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProveedorController.class)
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProveedorUseCase proveedorUseCase;

    @MockitoBean
    private ProveedorRestMapper proveedorRestMapper;

    @Test
    void crear_DebeRetornar201() throws Exception {
        ProveedorRequest request = new ProveedorRequest(1, "Prov1", "123", "a@a.com", "dir", "nit");
        ProveedorResponse response = new ProveedorResponse(1, null, "Prov1", "123", "a@a.com", "dir", "nit");

        when(proveedorRestMapper.toDomain(any())).thenReturn(new Proveedor());
        when(proveedorUseCase.crear(any())).thenReturn(new Proveedor());
        when(proveedorRestMapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(post("/api/catalog/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProveedor").value(1));
    }

    @Test
    void obtenerPorId_NotFound_404() throws Exception {
        when(proveedorUseCase.obtenerPorId(99)).thenThrow(new EntityNotFoundException("No encontrado"));

        mockMvc.perform(get("/api/catalog/proveedores/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No encontrado"));
    }
}
