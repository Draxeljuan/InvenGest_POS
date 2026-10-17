package com.invengest.transaction.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    private Integer idVenta;
    private Integer idUsuario;
    private LocalDateTime fecha;
    private Cliente cliente;
    private MetodoPago metodoPago;
    private BigDecimal total;
    private List<DetalleVenta> detalles = new ArrayList<>();
}
