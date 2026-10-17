package com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        Integer idTipoDocumento,

        @Size(max = 50, message = "El documento no puede exceder 50 caracteres")
        String documentoIdentidad,

        @NotBlank(message = "El primer nombre es obligatorio")
        @Size(max = 50, message = "El primer nombre no puede exceder 50 caracteres")
        String primerNombre,

        @Size(max = 50, message = "El segundo nombre no puede exceder 50 caracteres")
        String segundoNombre,

        @NotBlank(message = "El primer apellido es obligatorio")
        @Size(max = 50, message = "El primer apellido no puede exceder 50 caracteres")
        String primerApellido,

        @Size(max = 50, message = "El segundo apellido no puede exceder 50 caracteres")
        String segundoApellido,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String telefono,

        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        String email
) {}
