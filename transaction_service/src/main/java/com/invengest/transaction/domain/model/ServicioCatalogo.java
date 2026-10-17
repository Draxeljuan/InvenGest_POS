package com.invengest.transaction.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioCatalogo {
    private Integer idServicio;
    private String nombre;
    private String descripcion;
    private BigDecimal precioSugerido;
}
