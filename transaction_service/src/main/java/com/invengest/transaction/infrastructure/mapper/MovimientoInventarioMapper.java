package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.MovimientoInventario;
import com.invengest.transaction.infrastructure.driver_adapter.entity.MovimientoInventarioData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TipoMovimientoMapper.class})
public interface MovimientoInventarioMapper {
    MovimientoInventario toDomain(MovimientoInventarioData entity);
    MovimientoInventarioData toEntity(MovimientoInventario domain);
}
