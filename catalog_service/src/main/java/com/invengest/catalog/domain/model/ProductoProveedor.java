package com.invengest.catalog.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoProveedor {

    private String idProducto;
    private Integer idProveedor;
    private BigDecimal precioCompraProveedor;


}