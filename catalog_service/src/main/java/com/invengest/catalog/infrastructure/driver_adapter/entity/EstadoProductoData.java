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
@Table(name = "estado_producto")
public class EstadoProductoData {
    @NonNull
    @OneToMany
    @JoinColumn(name = "id_estado")
    private Set<ProductoData> productos = new LinkedHashSet<>();
    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado", nullable = false)
    private Integer id;
}
