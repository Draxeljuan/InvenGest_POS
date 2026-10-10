package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.Categoria;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.CategoriaRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.CategoriaResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaRestMapper {
    Categoria toDomain(CategoriaRequest request);
    CategoriaResponse toResponse(Categoria domain);
}
