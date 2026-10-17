package com.invengest.transaction.infrastructure.driver_adapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_documento")
    private TipoDocumentoData tipoDocumento;

    @Size(max = 50)
    @Column(name = "documento_identidad", unique = true, length = 50)
    private String documentoIdentidad;

    @NotNull
    @Size(max = 50)
    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;

    @Size(max = 50)
    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;

    @NotNull
    @Size(max = 50)
    @Column(name = "primer_apellido", nullable = false, length = 50)
    private String primerApellido;

    @Size(max = 50)
    @Column(name = "segundo_apellido", length = 50)
    private String segundoApellido;

    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;
}
