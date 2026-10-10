package com.invengest.catalog.infrastructure.mapper;

import com.invengest.catalog.domain.model.ProductoProveedor;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.request.ProductoProveedorRequest;
import com.invengest.catalog.infrastructure.driver_adapter.rest.dto.response.ProductoProveedorResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoProveedorRestMapper {
    ProductoProveedor toDomain(ProductoProveedorRequest request);
    ProductoProveedorResponse toResponse(ProductoProveedor domain);
}
