package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.Venta;
import com.invengest.transaction.infrastructure.driver_adapter.entity.VentaData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, MetodoPagoMapper.class, DetalleVentaMapper.class})
public interface VentaMapper {
    Venta toDomain(VentaData entity);
    VentaData toEntity(Venta domain);
}
