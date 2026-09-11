package com.invengest.auth.domain.usecase;

import com.invengest.auth.domain.exception.InvalidCredentialsException;
import com.invengest.auth.domain.gateway.PasswordEncoderGateway;
import com.invengest.auth.domain.gateway.TokenProviderGateway;
import com.invengest.auth.domain.gateway.UserRepository;
import com.invengest.auth.domain.model.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderGateway passwordEncoder;
    private final TokenProviderGateway tokenProvider;

    /**
     * Orquesta el flujo principal y alternativo de Iniciar Sesión
     */
    public String execute(String username, String rawPassword) {
        // Buscar el usuario en la base de datos
        User user = userRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new InvalidCredentialsException("Usuario o contraseña incorrectos"));

        // Validar que la contraseña digitada coincida con el hash de la BD
        if (!passwordEncoder.matches(rawPassword, user.getContrasena())) {
            throw new InvalidCredentialsException("Usuario o contraseña incorrectos");
        }

        // Actualizar la fecha de último acceso
        user.setUltimoAcceso(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.save(user);

        // Generar y retornar el token JWT seguro con el rol del usuario
        return tokenProvider.generateToken(user);
    }
}
