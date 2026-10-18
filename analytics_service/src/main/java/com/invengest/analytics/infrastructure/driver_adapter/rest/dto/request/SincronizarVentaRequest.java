package com.invengest.analytics.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SincronizarVentaRequest {

    private LocalDate fecha;

    @NotNull(message = "El totalVenta es obligatorio")
    @DecimalMin(value = "0.00", message = "El totalVenta no puede ser negativo")
    private BigDecimal totalVenta;

    @Valid
    private List<ItemVentaSincronizacionRequest> items;
}
