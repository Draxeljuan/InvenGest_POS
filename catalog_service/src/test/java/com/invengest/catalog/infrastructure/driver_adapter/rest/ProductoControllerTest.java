package com.invengest.catalog.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.catalog.domain.exception.DuplicateEntityException;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.model.Producto;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.ProductoRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.ProductoResponse;
import com.invengest.catalog.infrastructure.mapper.ProductoRestMapper;
import com.invengest.catalog.usecase.ProductoUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoUseCase productoUseCase;

    @MockitoBean
    private ProductoRestMapper productoRestMapper;

    @Test
    void registrar_DebeRetornar201() throws Exception {
        ProductoRequest request = new ProductoRequest(1, "Prod1", new BigDecimal("10.0"), new BigDecimal("5.0"), 10, 5, "Estante 1", 1);
        Producto dominio = new Producto();
        Producto registrado = new Producto();
        ProductoResponse response = new ProductoResponse("PROD-1111", null, "Prod1", null, null, null, null, null, null, null);

        when(productoRestMapper.toDomain(any(ProductoRequest.class))).thenReturn(dominio);
        when(productoUseCase.registrar(any(Producto.class))).thenReturn(registrado);
        when(productoRestMapper.toResponse(any(Producto.class))).thenReturn(response);

        mockMvc.perform(post("/api/catalog/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto").value("PROD-1111"));
    }

    @Test
    void registrar_BadRequest_Validaciones() throws Exception {
        // Falta categoría
        ProductoRequest request = new ProductoRequest(null, "Prod1", new BigDecimal("10.0"), new BigDecimal("5.0"), 10, 5, "Estante 1", 1);
        
        mockMvc.perform(post("/api/catalog/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Error de Validación"))
                .andExpect(jsonPath("$.invalid_params.idCategoria").exists());
    }

    @Test
    void obtenerPorId_NotFound_404() throws Exception {
        when(productoUseCase.obtenerPorId("PROD-9999")).thenThrow(new EntityNotFoundException("No encontrado"));

        mockMvc.perform(get("/api/catalog/productos/PROD-9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("No encontrado"));
    }
}
