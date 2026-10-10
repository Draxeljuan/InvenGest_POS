package com.invengest.catalog.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityInUseException;
import com.invengest.catalog.domain.model.Categoria;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.CategoriaRequest;
import com.invengest.catalog.infrastructure.mapper.CategoriaRestMapper;
import com.invengest.catalog.usecase.CategoriaUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoriaUseCase categoriaUseCase;

    @MockitoBean
    private CategoriaRestMapper categoriaRestMapper;

    @Test
    void crear_Conflict_409() throws Exception {
        CategoriaRequest request = new CategoriaRequest("Electrónica", "Desc");
        
        when(categoriaRestMapper.toDomain(any())).thenReturn(new Categoria());
        when(categoriaUseCase.crear(any())).thenThrow(new DuplicateEntityException("Ya existe"));

        mockMvc.perform(post("/api/catalog/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Ya existe"));
    }

    @Test
    void eliminar_Conflict_EntityInUse_409() throws Exception {
        doThrow(new EntityInUseException("En uso")).when(categoriaUseCase).eliminarPorId(1);

        mockMvc.perform(delete("/api/catalog/categorias/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("En uso"));
    }
}
