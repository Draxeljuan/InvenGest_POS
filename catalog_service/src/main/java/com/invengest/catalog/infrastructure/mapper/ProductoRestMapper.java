package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.Producto;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.ProductoRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.ProductoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoriaRestMapper.class, EstadoProductoRestMapper.class})
public interface ProductoRestMapper {
    @Mapping(target = "categoria.idCategoria", source = "idCategoria")
    @Mapping(target = "estado.idEstado", source = "idEstado")
    Producto toDomain(ProductoRequest request);

    ProductoResponse toResponse(Producto domain);
}
