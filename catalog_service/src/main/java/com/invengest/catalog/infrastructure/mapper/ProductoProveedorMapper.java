package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.ProductoProveedor;
import com.invengest.catalog.infrastructure.driver_adapter.entity.ProductoProveedorData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoProveedorMapper {

    @Mapping(source = "id.idProducto", target = "idProducto")
    @Mapping(source = "id.idProveedor", target = "idProveedor")
    ProductoProveedor toDomain(ProductoProveedorData entity);

    @Mapping(source = "idProducto", target = "id.idProducto")
    @Mapping(source = "idProveedor", target = "id.idProveedor")
    ProductoProveedorData toEntity(ProductoProveedor domain);
}