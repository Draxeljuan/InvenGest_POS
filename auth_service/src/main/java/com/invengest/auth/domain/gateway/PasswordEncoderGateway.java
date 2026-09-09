package com.invengest.auth.domain.gateway;

public interface PasswordEncoderGateway {
    boolean matches(String rawPassword, String encodedPassword);
    String encode(String rawPassword);
}