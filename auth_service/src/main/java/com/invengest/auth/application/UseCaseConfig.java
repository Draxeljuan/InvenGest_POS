package com.invengest.auth.application;

import com.invengest.auth.domain.gateway.PasswordEncoderGateway;
import com.invengest.auth.domain.gateway.TokenProviderGateway;
import com.invengest.auth.domain.gateway.UserRepository;
import com.invengest.auth.domain.usecase.LoginUseCase;
import com.invengest.auth.domain.usecase.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository,
                                     PasswordEncoderGateway passwordEncoder,
                                     TokenProviderGateway tokenProvider) {

        // Aquí conectamos el mundo de Spring (las implementaciones reales)
        // con nuestro mundo puro del Dominio.
        return new LoginUseCase(userRepository, passwordEncoder, tokenProvider);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
                                                   PasswordEncoderGateway passwordEncoder) {
        return new RegisterUserUseCase(userRepository, passwordEncoder);
    }
}