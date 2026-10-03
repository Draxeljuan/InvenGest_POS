package com.invengest.api_gateway.domain.gateway;

import com.invengest.api_gateway.domain.model.JwtPayload;

public interface TokenValidatorGateway {
    JwtPayload validateAndExtract(String token);
}
