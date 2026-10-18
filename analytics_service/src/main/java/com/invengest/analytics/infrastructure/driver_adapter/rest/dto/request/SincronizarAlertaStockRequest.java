package com.invengest.analytics.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SincronizarAlertaStockRequest {

    @NotBlank(message = "El idProducto es obligatorio")
    private String idProducto;

    @NotBlank(message = "El nombreProducto es obligatorio")
    private String nombreProducto;

    @NotNull(message = "El stockActual es obligatorio")
    private Short stockActual;
}
