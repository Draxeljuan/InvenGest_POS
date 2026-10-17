package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.MetodoPago;
import com.invengest.transaction.infrastructure.driver_adapter.entity.MetodoPagoData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetodoPagoMapper {
    MetodoPago toDomain(MetodoPagoData entity);
    MetodoPagoData toEntity(MetodoPago domain);
}
