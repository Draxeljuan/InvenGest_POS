package com.invengest.catalog.infrastructure.driver_adapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Table(name = "proveedor")
public class ProveedorData {
    @NonNull
    @OneToMany(mappedBy = "proveedor")
    private Set<ProductoProveedorData> productoProveedors = new LinkedHashSet<>();
    @Size(max = 50)
    @Column(name = "nit", length = 50)
    private String nit;
    @Size(max = 200)
    @Column(name = "direccion", length = 200)
    private String direccion;
    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;
    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;
    @Size(max = 150)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false)
    private Integer idProveedor;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoProveedorData estado;
}
