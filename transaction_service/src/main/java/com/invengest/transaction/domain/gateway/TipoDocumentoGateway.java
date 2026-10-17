package com.invengest.transaction.domain.gateway;

import com.invengest.transaction.domain.model.TipoDocumento;

import java.util.List;
import java.util.Optional;

public interface TipoDocumentoGateway {
    Optional<TipoDocumento> findById(Integer idTipoDocumento);
    List<TipoDocumento> findAll();
}
