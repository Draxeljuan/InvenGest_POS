package com.invengest.analytics.infrastructure.mapper;

import com.invengest.analytics.domain.model.DashboardResumen;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response.DashboardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        ResumenVentaMapper.class,
        TopProductoMapper.class,
        AlertaStockMapper.class
})
public interface DashboardRestMapper {
    DashboardResponse toResponse(DashboardResumen domain);
}
