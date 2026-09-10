package com.invengest.auth.infrastructure.driver_adapter.jpa_repository;

import com.invengest.auth.infrastructure.driver_adapter.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByNombreUsuario(String nombreUsuario);
}