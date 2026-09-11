package com.invengest.auth.domain.usecase;

import com.invengest.auth.domain.exception.UserAlreadyExistsException;
import com.invengest.auth.domain.gateway.PasswordEncoderGateway;
import com.invengest.auth.domain.gateway.UserRepository;
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
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderGateway passwordEncoder;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setNombreUsuario("JuanD");
    }

    @Test
    void execute_ShouldThrowException_WhenUserAlreadyExists() {
        // Arrange (Preparar): Simulamos que el repositorio encuentra al usuario
        when(userRepository.findByNombreUsuario("JuanD")).thenReturn(Optional.of(testUser));

        // Act & Assert (Actuar y Afirmar): Verificamos que se lance la excepción de Dominio
        assertThrows(UserAlreadyExistsException.class,
                () -> registerUserUseCase.execute(testUser, "password123"));

        // Verificamos que NUNCA se llame al método de guardar
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void execute_ShouldEncodePasswordAndSave_WhenUserIsNew() {
        // Arrange: Simulamos que el usuario no existe y el encriptador funciona
        when(userRepository.findByNombreUsuario("JuanD")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act: Ejecutamos el caso de uso
        User result = registerUserUseCase.execute(testUser, "password123");

        // Assert: Validamos que la contraseña se haya cifrado y guardado
        assertEquals("hashed_password", testUser.getContrasena());
        assertNotNull(result);
        verify(userRepository, times(1)).save(testUser);
    }
}