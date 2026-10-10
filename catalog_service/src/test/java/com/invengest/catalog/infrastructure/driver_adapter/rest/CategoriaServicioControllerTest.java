package com.invengest.catalog.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.catalog.domain.model.CategoriaServicio;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.CategoriaServicioRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.CategoriaServicioResponse;
import com.invengest.catalog.infrastructure.mapper.CategoriaServicioRestMapper;
import com.invengest.catalog.usecase.CategoriaServicioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaServicioController.class)
class CategoriaServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoriaServicioUseCase categoriaServicioUseCase;

    @MockitoBean
    private CategoriaServicioRestMapper categoriaServicioRestMapper;

    @Test
    void crear_DebeRetornar201() throws Exception {
        CategoriaServicioRequest request = new CategoriaServicioRequest("CatServ1");
        CategoriaServicioResponse response = new CategoriaServicioResponse(1, "CatServ1");

        when(categoriaServicioRestMapper.toDomain(any())).thenReturn(new CategoriaServicio());
        when(categoriaServicioUseCase.crear(any())).thenReturn(new CategoriaServicio());
        when(categoriaServicioRestMapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(post("/api/catalog/categorias-servicio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("CatServ1"));
    }
}
