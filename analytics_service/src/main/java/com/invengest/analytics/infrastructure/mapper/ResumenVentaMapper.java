package com.invengest.analytics.infrastructure.mapper;

import com.invengest.analytics.domain.model.ResumenVentaDiaria;
import com.invengest.analytics.infrastructure.driver_adapter.entity.ResumenVentaDiariaData;
import com.invengest.analytics.infrastructure.driver_adapter.rest.dto.response.ResumenVentaResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResumenVentaMapper {
    ResumenVentaDiaria toDomain(ResumenVentaDiariaData entity);
    ResumenVentaDiariaData toEntity(ResumenVentaDiaria domain);
    ResumenVentaResponse toResponse(ResumenVentaDiaria domain);
    List<ResumenVentaResponse> toResponseList(List<ResumenVentaDiaria> domainList);
}
