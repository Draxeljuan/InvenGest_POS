package com.invengest.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class User {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDateTime ultimoAcceso;
    private Role rol; // Composición del rol
    private String nombreUsuario;
    private String contrasena;

}