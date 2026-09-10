package com.invengest.auth.infrastructure.driver_adapter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "rol_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;
    @NonNull
    @OneToMany(mappedBy = "rol")
    private Set<UserEntity> usuarios = new LinkedHashSet<>();
}