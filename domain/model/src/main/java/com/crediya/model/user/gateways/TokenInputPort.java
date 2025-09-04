package com.crediya.model.user.gateways;

public interface TokenInputPort {
    String generateToken(String subject, String role);
}
