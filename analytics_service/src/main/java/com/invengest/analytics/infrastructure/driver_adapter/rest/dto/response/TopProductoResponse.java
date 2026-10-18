package com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProductoResponse {
    private Integer idTop;
    private String idProducto;
    private String nombreProducto;
    private Integer cantidadVendida;
    private LocalDate fecha;
}
