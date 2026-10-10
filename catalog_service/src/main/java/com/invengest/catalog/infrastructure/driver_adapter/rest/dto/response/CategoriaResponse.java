package com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response;

public record CategoriaResponse(
        Integer idCategoria,
        String nombre,
        String descripcion
) {}
