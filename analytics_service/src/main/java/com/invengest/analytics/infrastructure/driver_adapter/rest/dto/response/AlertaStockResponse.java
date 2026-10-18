package com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaStockResponse {
    private Integer idAlerta;
    private String idProducto;
    private String nombreProducto;
    private Short stockActual;
    private String estado;
    private LocalDateTime fechaAlerta;
}
