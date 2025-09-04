package com.crediya.usecase.auth;

import com.crediya.model.user.User;
import com.crediya.model.user.gateways.AuthInputPort;
import com.crediya.model.user.gateways.PasswordEncoderInputPort;
import com.crediya.model.user.gateways.TokenInputPort;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.usecase.exception.ArgumentException;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

public class AuthUseCase implements AuthInputPort {

    private final UserRepository userRepository;
    private final TokenInputPort tokenInputPort;
    private final PasswordEncoderInputPort passwordEncoderInputPort;

    private static final Logger logger = Logger.getLogger(AuthUseCase.class.getName());
    private static final String INVALID_CREDENTIALS_MSG = "Usuario o contraseña inválidos";

    public AuthUseCase(UserRepository userRepository,
                       TokenInputPort tokenInputPort,
                       PasswordEncoderInputPort passwordEncoderInputPort) {
        this.userRepository = userRepository;
        this.tokenInputPort = tokenInputPort;
        this.passwordEncoderInputPort = passwordEncoderInputPort;
    }

    @Override
    public Mono<String> authenticate(String email, String password) {
        return findUser(email)
                .flatMap(user -> validatePassword(user, password))
                .flatMap(this::generateToken);
    }

    private Mono<User> findUser(String email) {
        return userRepository.getUserByEmail(email)
                .switchIfEmpty(Mono.error(new ArgumentException(INVALID_CREDENTIALS_MSG)))
                .doOnNext(user -> logger.info("Usuario encontrado: " + email));
    }

    private Mono<User> validatePassword(User user, String rawPassword) {
        if (!passwordEncoderInputPort.matches(rawPassword, user.getPassword())) {
            logger.info("Credenciales inválidas para usuario: " + user.getEmail());
            return Mono.error(new ArgumentException(INVALID_CREDENTIALS_MSG));
        }
        logger.info("Contraseña válida para usuario: " + user.getEmail());
        return Mono.just(user);
    }

    private Mono<String> generateToken(User user) {
        return Mono.just(buildToken(user));
    }

    private String buildToken(User user) {
        return tokenInputPort.generateToken(
                user.getEmail(),
                user.getRoleName()
        );
    }
}