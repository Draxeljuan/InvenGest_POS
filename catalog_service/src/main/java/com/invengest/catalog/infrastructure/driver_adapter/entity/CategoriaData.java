package com.invengest.catalog.infrastructure.driver_adapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "categoria")
public class CategoriaData {
    @NonNull
    @OneToMany
    @JoinColumn(name = "id_categoria")
    private Set<ProductoData> productos = new LinkedHashSet<>();
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria", nullable = false)
    private Integer id;


}
