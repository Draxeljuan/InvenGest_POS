package com.invengest.auth.domain.usecase;

import com.invengest.auth.domain.exception.UserAlreadyExistsException;
import com.invengest.auth.domain.gateway.PasswordEncoderGateway;
import com.invengest.auth.domain.gateway.UserRepository;
import com.invengest.auth.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderGateway passwordEncoder;

    public User execute(User user, String rawPassword) {
        // Validar que el nombre de usuario no esté tomado
        if (userRepository.findByNombreUsuario(user.getNombreUsuario()).isPresent()) {
            throw new UserAlreadyExistsException("El nombre de usuario ya está registrado");
        }

        // Cifrar la contraseña antes de guardarla en la BD
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setContrasena(encodedPassword);

        // Persistir usando el puerto de salida
        return userRepository.save(user);
    }
}