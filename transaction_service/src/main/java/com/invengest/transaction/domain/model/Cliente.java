package com.invengest.transaction.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private Integer idCliente;
    private TipoDocumento tipoDocumento;
    private String documentoIdentidad;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;
    private String email;

    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        if (primerNombre != null) sb.append(primerNombre);
        if (segundoNombre != null && !segundoNombre.isBlank()) sb.append(" ").append(segundoNombre);
        if (primerApellido != null) sb.append(" ").append(primerApellido);
        if (segundoApellido != null && !segundoApellido.isBlank()) sb.append(" ").append(segundoApellido);
        return sb.toString().trim();
    }
}
