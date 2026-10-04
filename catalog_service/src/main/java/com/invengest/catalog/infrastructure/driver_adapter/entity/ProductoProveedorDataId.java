package com.invengest.catalog.infrastructure.driver_adapter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ProductoProveedorDataId implements Serializable {
    @Serial
    private static final long serialVersionUID = -5280225435741267587L;
    @Size(max = 50)
    @NotNull
    @Column(name = "id_producto", nullable = false, length = 50)
    private String idProducto;

    @NotNull
    @Column(name = "id_proveedor", nullable = false)
    private Integer idProveedor;


}