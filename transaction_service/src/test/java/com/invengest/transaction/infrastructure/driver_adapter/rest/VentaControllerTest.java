package com.invengest.transaction.infrastructure.driver_adapter.rest;

import tools.jackson.databind.ObjectMapper;
import com.invengest.transaction.domain.exception.EntityNotFoundException;
import com.invengest.transaction.domain.model.TipoItem;
import com.invengest.transaction.domain.model.Venta;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.DetalleVentaRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.RegistrarVentaRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.VentaResponse;
import com.invengest.transaction.infrastructure.mapper.VentaRestMapper;
import com.invengest.transaction.usecase.ConsultarVentaUseCase;
import com.invengest.transaction.usecase.RegistrarVentaUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentaController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegistrarVentaUseCase registrarVentaUseCase;

    @MockitoBean
    private ConsultarVentaUseCase consultarVentaUseCase;

    @MockitoBean
    private VentaRestMapper ventaRestMapper;

    @Test
    @DisplayName("POST /api/transactions/ventas - Registrar venta exitosa retorna 201 Created")
    void registrar_DebeRetornar201() throws Exception {
        DetalleVentaRequest itemRequest = new DetalleVentaRequest("PROD-001", TipoItem.PRODUCTO, null, null, 2);
        RegistrarVentaRequest request = new RegistrarVentaRequest(1, 10, 1, List.of(itemRequest));

        Venta dominio = new Venta();
        Venta registrada = new Venta();
        registrada.setIdVenta(1);
        VentaResponse response = new VentaResponse(1, 1, LocalDateTime.now(), null, null, new BigDecimal("10000.00"), List.of());

        when(ventaRestMapper.toDomain(any(RegistrarVentaRequest.class))).thenReturn(dominio);
        when(registrarVentaUseCase.registrarVenta(any(Venta.class))).thenReturn(registrada);
        when(ventaRestMapper.toResponse(any(Venta.class))).thenReturn(response);

        mockMvc.perform(post("/api/transactions/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVenta").value(1))
                .andExpect(jsonPath("$.total").value(10000.00));
    }

    @Test
    @DisplayName("POST /api/transactions/ventas - Error 400 por validación de campos")
    void registrar_BadRequest_ValidacionCampos() throws Exception {
        // idUsuario e idMetodoPago nulos
        RegistrarVentaRequest request = new RegistrarVentaRequest(null, null, null, List.of());

        mockMvc.perform(post("/api/transactions/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Error de Validación"))
                .andExpect(jsonPath("$.invalid_params.idUsuario").exists())
                .andExpect(jsonPath("$.invalid_params.idMetodoPago").exists());
    }

    @Test
    @DisplayName("GET /api/transactions/ventas/{id} - Obtener venta existente retorna 200 OK")
    void obtenerPorId_Exitoso() throws Exception {
        Venta venta = new Venta();
        venta.setIdVenta(1);
        VentaResponse response = new VentaResponse(1, 1, LocalDateTime.now(), null, null, new BigDecimal("5000.00"), List.of());

        when(consultarVentaUseCase.obtenerPorId(1)).thenReturn(venta);
        when(ventaRestMapper.toResponse(venta)).thenReturn(response);

        mockMvc.perform(get("/api/transactions/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVenta").value(1));
    }

    @Test
    @DisplayName("GET /api/transactions/ventas/{id} - Venta no encontrada retorna 404 Not Found")
    void obtenerPorId_NoExiste_404() throws Exception {
        when(consultarVentaUseCase.obtenerPorId(999)).thenThrow(new EntityNotFoundException("Venta no encontrada con ID: 999"));

        mockMvc.perform(get("/api/transactions/ventas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.detail").value("Venta no encontrada con ID: 999"));
    }
}
