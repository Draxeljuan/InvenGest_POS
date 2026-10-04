package com.invengest.catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;


}