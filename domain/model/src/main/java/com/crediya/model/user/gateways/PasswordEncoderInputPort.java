package com.crediya.model.user.gateways;

public interface PasswordEncoderInputPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
