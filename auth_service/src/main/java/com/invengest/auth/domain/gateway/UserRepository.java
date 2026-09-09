package com.invengest.auth.domain.gateway;

import com.invengest.auth.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByNombreUsuario(String nombreUsuario);
    User save(User user);
}
