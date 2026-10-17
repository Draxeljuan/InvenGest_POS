package com.invengest.transaction.infrastructure.driver_adapter.jpa_repository.interfaces;

import com.invengest.transaction.infrastructure.driver_adapter.entity.MetodoPagoData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetodoPagoJpaRepository extends JpaRepository<MetodoPagoData, Integer> {
}
