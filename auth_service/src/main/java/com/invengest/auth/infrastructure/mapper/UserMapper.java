package com.invengest.auth.infrastructure.mapper;

import com.invengest.auth.domain.model.Role;
import com.invengest.auth.domain.model.User;
import com.invengest.auth.infrastructure.driver_adapter.entity.RoleEntity;
import com.invengest.auth.infrastructure.driver_adapter.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Mapeo Usuario
    User toDomain(UserEntity entity);
    UserEntity toEntity(User domain);

    // Mapeo Rol (usado implícitamente por el mapeo de Usuario)
    Role toDomain(RoleEntity entity);
    RoleEntity toEntity(Role domain);
}
