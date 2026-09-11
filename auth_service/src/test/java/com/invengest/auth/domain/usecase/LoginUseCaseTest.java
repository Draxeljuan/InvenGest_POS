package com.invengest.auth.domain.usecase;

import com.invengest.auth.domain.exception.InvalidCredentialsException;
import com.invengest.auth.domain.gateway.PasswordEncoderGateway;
import com.invengest.auth.domain.gateway.TokenProviderGateway;
import com.invengest.auth.domain.gateway.UserRepository;
import com.invengest.auth.domain.model.Role;
import com.invengest.auth.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderGateway passwordEncoder;

    @Mock
    private TokenProviderGateway tokenProvider;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setIdUsuario(1);
        testUser.setNombreUsuario("JuanD");
        testUser.setContrasena("hashed_password");
        testUser.setRol(new Role(1, "Administrador"));
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        // Arrange: Simulamos que el usuario no existe en la base de datos
        when(userRepository.findByNombreUsuario("JuanD")).thenReturn(Optional.empty());

        // Act & Assert: Validamos que lance la excepción de credenciales inválidas
        assertThrows(InvalidCredentialsException.class,
                () -> loginUseCase.execute("JuanD", "password123"));
    }

    @Test
    void execute_ShouldThrowException_WhenPasswordDoesNotMatch() {
        // Arrange: Usuario existe, pero el encriptador dice que la contraseña no coincide
        when(userRepository.findByNombreUsuario("JuanD")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrong_password", "hashed_password")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class,
                () -> loginUseCase.execute("JuanD", "wrong_password"));
    }

    @Test
    void execute_ShouldReturnTokenAndSaveAccess_WhenCredentialsAreValid() {
        // Arrange: Flujo feliz. Usuario existe, contraseña coincide, token se genera.
        when(userRepository.findByNombreUsuario("JuanD")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
        when(tokenProvider.generateToken(testUser)).thenReturn("jwt.token.here");

        // Simular el guardado de la actualización del último acceso
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        String token = loginUseCase.execute("JuanD", "password123");

        // Assert
        assertEquals("jwt.token.here", token);
        assertNotNull(testUser.getUltimoAcceso()); // Validar que se asignó la fecha de acceso
        verify(userRepository, times(1)).save(testUser); // Validar que se llamó al repositorio para actualizar
    }
}
