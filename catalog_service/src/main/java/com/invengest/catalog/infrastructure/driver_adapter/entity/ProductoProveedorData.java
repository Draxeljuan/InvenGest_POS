package com.invengest.catalog.infrastructure.driver_adapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "producto_proveedor")
public class ProductoProveedorData {
    @NotNull
    @Column(name = "precio_compra_proveedor", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioCompraProveedor;
    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private ProveedorData idProveedor;
    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoData idProducto;
    @EmbeddedId
    private ProductoProveedorDataId id;
}
