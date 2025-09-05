package com.crediya.api.service;

import com.crediya.api.security.JwtUtil;
import com.crediya.model.user.gateways.AuthInputPort;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final AuthInputPort authInputPort;

    public AuthService(AuthInputPort authInputPort) {
        this.authInputPort = authInputPort;
    }

    public Mono<String> authenticate(String email, String password) {
        return authInputPort.authenticate(email, password);
    }
}

