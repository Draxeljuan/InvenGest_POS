package com.invengest.analytics.infrastructure.mapper;

import com.invengest.analytics.domain.model.TopProducto;
import com.invengest.analytics.infrastructure.driver_adapter.entity.TopProductoData;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response.TopProductoResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopProductoMapper {
    TopProducto toDomain(TopProductoData entity);
    TopProductoData toEntity(TopProducto domain);
    TopProductoResponse toResponse(TopProducto domain);
    List<TopProductoResponse> toResponseList(List<TopProducto> domainList);
}
