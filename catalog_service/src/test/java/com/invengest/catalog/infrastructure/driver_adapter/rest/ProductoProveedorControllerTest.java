package com.invengest.catalog.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.catalog.domain.exception.EntityNotFoundException;
import com.invengest.catalog.domain.model.ProductoProveedor;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.ProductoProveedorRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.ProductoProveedorResponse;
import com.invengest.catalog.infrastructure.mapper.ProductoProveedorRestMapper;
import com.invengest.catalog.usecase.ProductoProveedorUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoProveedorController.class)
class ProductoProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoProveedorUseCase productoProveedorUseCase;

    @MockitoBean
    private ProductoProveedorRestMapper productoProveedorRestMapper;

    @Test
    void obtenerPorProducto_Success_200() throws Exception {
        ProductoProveedor domain = new ProductoProveedor();
        ProductoProveedorResponse response = new ProductoProveedorResponse("PROD-1", 1, new BigDecimal("10.0"));
        
        when(productoProveedorUseCase.obtenerPorProducto("PROD-1")).thenReturn(List.of(domain));
        when(productoProveedorRestMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/api/catalog/productos-proveedores/producto/PROD-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value("PROD-1"));
    }

    @Test
    void obtenerPorProveedor_Success_200() throws Exception {
        ProductoProveedor domain = new ProductoProveedor();
        ProductoProveedorResponse response = new ProductoProveedorResponse("PROD-1", 1, new BigDecimal("10.0"));
        
        when(productoProveedorUseCase.obtenerPorProveedor(1)).thenReturn(List.of(domain));
        when(productoProveedorRestMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/api/catalog/productos-proveedores/proveedor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProveedor").value(1));
    }

    @Test
    void asociarOActualizar_Success_201() throws Exception {
        ProductoProveedorRequest request = new ProductoProveedorRequest("PROD-1", 1, new BigDecimal("10.0"));
        ProductoProveedor domain = new ProductoProveedor();
        ProductoProveedorResponse response = new ProductoProveedorResponse("PROD-1", 1, new BigDecimal("10.0"));

        when(productoProveedorRestMapper.toDomain(any())).thenReturn(domain);
        when(productoProveedorUseCase.asociarOActualizar(any())).thenReturn(domain);
        when(productoProveedorRestMapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(post("/api/catalog/productos-proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto").value("PROD-1"));
    }

    @Test
    void asociarOActualizar_BadRequest_400() throws Exception {
        ProductoProveedorRequest request = new ProductoProveedorRequest("", null, new BigDecimal("-1.0"));

        mockMvc.perform(post("/api/catalog/productos-proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarAsociacion_Success_204() throws Exception {
        doNothing().when(productoProveedorUseCase).eliminarAsociacion(anyString(), anyInt());

        mockMvc.perform(delete("/api/catalog/productos-proveedores/producto/PROD-1/proveedor/1"))
                .andExpect(status().isNoContent());
    }
}
