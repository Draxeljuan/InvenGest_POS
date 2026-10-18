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

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_stock")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaStockData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Integer idAlerta;

    @Column(name = "id_producto", nullable = false, length = 50)
    private String idProducto;

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "stock_actual", nullable = false)
    private Short stockActual;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "fecha_alerta", nullable = false)
    private LocalDateTime fechaAlerta;
}
