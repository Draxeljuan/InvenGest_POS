package com.invengest.auth.infrastructure.driver_adapter.jpa_repository;

import com.invengest.auth.domain.gateway.UserRepository;
import com.invengest.auth.domain.model.User;
import com.invengest.auth.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findByNombreUsuario(String nombreUsuario) {
        return userJpaRepository.findByNombreUsuario(nombreUsuario)
                // Usamos el mapper para devolver un objeto puro del Dominio
                .map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        // Convertimos el Dominio a Entity para que JPA pueda guardarlo
        var entityToSave = userMapper.toEntity(user);
        var savedEntity = userJpaRepository.save(entityToSave);
        // Retornamos de nuevo al Dominio
        return userMapper.toDomain(savedEntity);
    }
}
