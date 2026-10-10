package com.invengest.catalog.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    private String idProducto;
    private Categoria categoria;
    private String nombre;
    private BigDecimal precioVenta;
    private BigDecimal costoCompra;
    private LocalDate fechaIngreso;
    private Integer stock;
    private Integer stockMinimo;
    private String ubicacion;
    private EstadoProducto estado;


}