package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.MovimientoInventario;
import com.invengest.transaction.domain.model.TipoMovimiento;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.MovimientoInventarioRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.MovimientoInventarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TipoMovimientoRestMapper.class})
public interface MovimientoInventarioRestMapper {

    MovimientoInventarioResponse toResponse(MovimientoInventario domain);

    @Mapping(target = "idMovimientoInventario", ignore = true)
    @Mapping(target = "fechaMovimiento", ignore = true)
    @Mapping(target = "tipoMovimiento", expression = "java(mapTipoMovimiento(request.idTipoMovimiento()))")
    MovimientoInventario toDomain(MovimientoInventarioRequest request);

    default TipoMovimiento mapTipoMovimiento(Integer idTipoMovimiento) {
        if (idTipoMovimiento == null) {
            return null;
        }
        TipoMovimiento tm = new TipoMovimiento();
        tm.setIdMovimiento(idTipoMovimiento);
        return tm;
    }
}
