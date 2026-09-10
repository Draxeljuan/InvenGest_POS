package com.invengest.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {

    private Integer idRol;
    private String nombre; // Ej: Administrador, Vendedor


}
