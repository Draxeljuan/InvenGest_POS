package com.invengest.catalog.infrastructure.driver_adapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "producto")
public class ProductoData {
    @NonNull
    @OneToMany(mappedBy = "idProducto")
    private Set<ProductoProveedorData> productoProveedors = new LinkedHashSet<>();
    @Size(max = 100)
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;
    @NotNull
    @ColumnDefault("0")
    @Column(name = "stock_minimo", nullable = false)
    private Short stockMinimo;
    @NotNull
    @ColumnDefault("0")
    @Column(name = "stock", nullable = false)
    private Short stock;
    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
    @NotNull
    @Column(name = "costo_compra", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoCompra;
    @NotNull
    @Column(name = "precio_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;
    @Size(max = 150)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Usamos UUID de momento
    @Size(max = 50)
    @Column(name = "id_producto", nullable = false, length = 50)
    private String idProducto;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoProductoData idEstado;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaData idCategoria;

}
