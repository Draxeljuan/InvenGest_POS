package com.invengest.auth.infrastructure.driver_adapter.rest.advice;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        List<String> messages
) {}