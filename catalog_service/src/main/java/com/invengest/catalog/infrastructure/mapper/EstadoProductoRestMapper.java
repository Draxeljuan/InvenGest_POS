package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.EstadoProducto;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.EstadoProductoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoProductoRestMapper {
    EstadoProductoResponse toResponse(EstadoProducto domain);
}
