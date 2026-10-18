package com.invengest.analytics.infrastructure.mapper;

import com.invengest.analytics.domain.model.AlertaStock;
import com.invengest.analytics.infrastructure.driver_adapter.entity.AlertaStockData;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response.AlertaStockResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlertaStockMapper {
    AlertaStock toDomain(AlertaStockData entity);
    AlertaStockData toEntity(AlertaStock domain);
    AlertaStockResponse toResponse(AlertaStock domain);
    List<AlertaStockResponse> toResponseList(List<AlertaStock> domainList);
}
