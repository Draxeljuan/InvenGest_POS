package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.CategoriaServicio;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.CategoriaServicioRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.CategoriaServicioResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaServicioRestMapper {
    CategoriaServicio toDomain(CategoriaServicioRequest request);
    CategoriaServicioResponse toResponse(CategoriaServicio domain);
}
