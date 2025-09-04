package com.crediya.api.service;

import com.crediya.api.security.JwtUtil;
import com.crediya.model.user.gateways.TokenInputPort;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAdapter implements TokenInputPort {

    private final JwtUtil jwtUtil;

    public JwtTokenAdapter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String generateToken(String subject, String role) {
        return jwtUtil.generateToken(subject, role);
    }
}
