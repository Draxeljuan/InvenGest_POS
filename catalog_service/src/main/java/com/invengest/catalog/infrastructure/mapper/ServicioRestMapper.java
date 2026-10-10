package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.Servicio;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.ServicioRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.ServicioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoriaServicioRestMapper.class})
public interface ServicioRestMapper {
    @Mapping(target = "categoriaServicio.idCategoriaServicio", source = "idCategoriaServicio")
    Servicio toDomain(ServicioRequest request);

    ServicioResponse toResponse(Servicio domain);
}
