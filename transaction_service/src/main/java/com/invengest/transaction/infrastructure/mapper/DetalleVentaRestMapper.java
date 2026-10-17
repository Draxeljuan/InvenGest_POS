package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.DetalleVenta;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.DetalleVentaRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.DetalleVentaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleVentaRestMapper {

    DetalleVentaResponse toResponse(DetalleVenta domain);

    @Mapping(target = "idDetalleVenta", ignore = true)
    @Mapping(target = "idVenta", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    DetalleVenta toDomain(DetalleVentaRequest request);
}
