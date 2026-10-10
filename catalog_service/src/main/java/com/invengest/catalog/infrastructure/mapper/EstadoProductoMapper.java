package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.EstadoProducto;
import com.invengest.catalog.infrastructure.driver_adapter.entity.EstadoProductoData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoProductoMapper {
    EstadoProducto toDomain(EstadoProductoData entity);
    EstadoProductoData toEntity(EstadoProducto domain);
}