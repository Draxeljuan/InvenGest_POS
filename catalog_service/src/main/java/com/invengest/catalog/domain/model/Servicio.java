package com.invengest.catalog.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    private Integer idServicio;
    private CategoriaServicio categoriaServicio;
    private String nombre;
    private String descripcion;
    private BigDecimal precioSugerido;

}