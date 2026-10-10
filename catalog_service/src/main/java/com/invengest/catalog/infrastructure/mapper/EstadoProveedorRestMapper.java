package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.EstadoProveedor;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.EstadoProveedorResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoProveedorRestMapper {
    EstadoProveedorResponse toResponse(EstadoProveedor domain);
}
