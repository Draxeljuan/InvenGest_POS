package com.invengest.analytics.infrastructure.driver_adapter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "top_productos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopProductoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_top")
    private Integer idTop;

    @Column(name = "id_producto", nullable = false, length = 50)
    private String idProducto;

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "cantidad_vendida", nullable = false)
    private Integer cantidadVendida;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
}
