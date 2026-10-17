package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.transaction.infrastructure.driver_adapter.entity.TipoDocumentoData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoDocumentoJpaRepository extends JpaRepository<TipoDocumentoData, Integer> {
}
