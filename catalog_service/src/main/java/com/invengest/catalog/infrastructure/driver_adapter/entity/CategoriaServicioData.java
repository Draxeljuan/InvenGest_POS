package com.invengest.catalog.infrastructure.driver_adapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Table(name = "categoria_servicio")
public class CategoriaServicioData {
    @NonNull
    @OneToMany
    @JoinColumn(name = "id_categoria_servicio")
    private Set<ServicioData> servicios = new LinkedHashSet<>();
    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_servicio", nullable = false)
    private Integer idCategoriaServicio;

}
