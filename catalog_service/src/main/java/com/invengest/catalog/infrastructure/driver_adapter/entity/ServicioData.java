package com.invengest.catalog.infrastructure.driver_adapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "servicio")
public class ServicioData {
    @NotNull
    @Column(name = "precio_sugerido", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioSugerido;
    @Column(name = "descripcion", length = Integer.MAX_VALUE)
    private String descripcion;
    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio", nullable = false)
    private Integer id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria_servicio", nullable = false)
    private CategoriaServicioData idCategoriaServicio;

}
