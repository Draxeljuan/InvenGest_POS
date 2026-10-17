package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.TipoMovimiento;
import com.invengest.transaction.infrastructure.driver_adapter.entity.TipoMovimientoData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoMovimientoMapper {
    TipoMovimiento toDomain(TipoMovimientoData entity);
    TipoMovimientoData toEntity(TipoMovimiento domain);
}
