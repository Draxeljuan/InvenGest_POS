package com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response;

public record ClienteResponse(
        Integer idCliente,
        TipoDocumentoResponse tipoDocumento,
        String documentoIdentidad,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        String nombreCompleto,
        String telefono,
        String email
) {}
