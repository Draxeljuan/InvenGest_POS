package com.invengest.auth.domain.gateway;

import com.invengest.auth.domain.model.User;

public interface TokenProviderGateway {
    String generateToken(User user);
}

