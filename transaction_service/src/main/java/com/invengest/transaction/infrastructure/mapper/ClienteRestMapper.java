package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.Cliente;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.request.ClienteRequest;
import com.invengest.transaction.infrastructure.driver_adapter.rest.dto.response.ClienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TipoDocumentoRestMapper.class})
public interface ClienteRestMapper {

    @Mapping(target = "nombreCompleto", expression = "java(domain.getNombreCompleto())")
    ClienteResponse toResponse(Cliente domain);

    @Mapping(target = "idCliente", ignore = true)
    @Mapping(target = "tipoDocumento.idTipoDocumento", source = "idTipoDocumento")
    Cliente toDomain(ClienteRequest request);
}
