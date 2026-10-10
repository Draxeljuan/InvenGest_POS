package com.invengest.catalog.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.model.Servicio;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.ServicioRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.ServicioResponse;
import com.invengest.catalog.infrastructure.mapper.ServicioRestMapper;
import com.invengest.catalog.usecase.ServicioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServicioController.class)
class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServicioUseCase servicioUseCase;

    @MockitoBean
    private ServicioRestMapper servicioRestMapper;

    @Test
    void registrar_Conflict_409() throws Exception {
        ServicioRequest request = new ServicioRequest(1, "Servicio1", "Desc", new BigDecimal("100"));
        
        when(servicioRestMapper.toDomain(any())).thenReturn(new Servicio());
        when(servicioUseCase.registrar(any())).thenThrow(new DuplicateEntityException("Duplicado"));

        mockMvc.perform(post("/api/catalog/servicios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Duplicado"));
    }
}
