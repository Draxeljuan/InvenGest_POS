package com.invengest.api_gateway.infraestructure.configuration;

import com.invengest.api_gateway.domain.gateway.TokenValidatorGateway;
import com.invengest.api_gateway.usecase.ValidateTokenUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ValidateTokenUseCase validateTokenUseCase(TokenValidatorGateway validatorGateway) {
        return new ValidateTokenUseCase(validatorGateway);
    }
}
