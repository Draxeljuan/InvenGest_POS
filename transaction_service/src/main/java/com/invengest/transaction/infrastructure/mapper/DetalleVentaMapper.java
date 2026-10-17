package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.DetalleVenta;
import com.invengest.transaction.infrastructure.driver_adapter.entity.DetalleVentaData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleVentaMapper {

    @Mapping(source = "venta.idVenta", target = "idVenta")
    DetalleVenta toDomain(DetalleVentaData entity);

    @Mapping(target = "venta", ignore = true)
    DetalleVentaData toEntity(DetalleVenta domain);
}
