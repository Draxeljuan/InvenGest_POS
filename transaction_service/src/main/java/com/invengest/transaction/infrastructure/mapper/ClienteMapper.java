package com.invengest.transaction.infrastructure.mapper;

import com.invengest.transaction.domain.model.Cliente;
import com.invengest.transaction.infrastructure.driver_adapter.entity.ClienteData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TipoDocumentoMapper.class})
public interface ClienteMapper {
    Cliente toDomain(ClienteData entity);
    ClienteData toEntity(Cliente domain);
}
