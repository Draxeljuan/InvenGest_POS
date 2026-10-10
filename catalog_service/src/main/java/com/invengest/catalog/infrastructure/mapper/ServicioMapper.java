package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.Servicio;
import com.invengest.catalog.infrastructure.driver_adapter.entity.ServicioData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoriaServicioMapper.class})
public interface ServicioMapper {
    Servicio toDomain(ServicioData entity);
    ServicioData toEntity(Servicio domain);
}