package com.invengest.analytics.infrastructure.driver_adapter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "resumen_ventas_diarias")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenVentaDiariaData {

    @Id
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "total_ventas", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalVentas;

    @Column(name = "cantidad_operaciones", nullable = false)
    private Integer cantidadOperaciones;
}
