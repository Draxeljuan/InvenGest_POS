package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.Producto;
import com.invengest.catalog.infrastructure.driver_adapter.entity.ProductoData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class, EstadoProductoMapper.class})
public interface ProductoMapper {
    Producto toDomain(ProductoData entity);
    ProductoData toEntity(Producto domain);
}