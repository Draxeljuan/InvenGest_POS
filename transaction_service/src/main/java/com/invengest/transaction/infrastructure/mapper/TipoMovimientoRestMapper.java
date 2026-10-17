package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.TipoMovimiento;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.TipoMovimientoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoMovimientoRestMapper {
    TipoMovimientoResponse toResponse(TipoMovimiento domain);
}
