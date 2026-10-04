package com.invengest.catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    private Integer idProveedor;
    private String estado;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private String nit;


}