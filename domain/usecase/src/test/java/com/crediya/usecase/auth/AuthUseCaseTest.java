package com.crediya.usecase.auth;

import com.crediya.model.user.User;
import com.crediya.model.user.gateways.PasswordEncoderInputPort;
import com.crediya.model.user.gateways.TokenInputPort;
import com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class AuthUseCaseTest {

    private UserRepository userRepository;
    private TokenInputPort tokenInputPort;
    private PasswordEncoderInputPort passwordEncoderInputPort;
    private AuthUseCase authUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        tokenInputPort = mock(TokenInputPort.class);
        passwordEncoderInputPort = mock(PasswordEncoderInputPort.class);

        authUseCase = new AuthUseCase(
                userRepository,
                tokenInputPort,
                passwordEncoderInputPort
        );
    }

    @Test
    void authenticateSuccess() {
        User user = new User();
        user.setId("1");
        user.setEmail("test@mail.com");
        user.setPassword("encodedPassword");
        user.setRoleName("ADMIN");

        when(userRepository.getUserByEmail("test@mail.com"))
                .thenReturn(Mono.just(user));
        when(passwordEncoderInputPort.matches("rawPassword", "encodedPassword"))
                .thenReturn(true);
        when(tokenInputPort.generateToken("test@mail.com", "ADMIN"))
                .thenReturn("mockToken");

        StepVerifier.create(authUseCase.authenticate("test@mail.com", "rawPassword"))
                .expectNext("mockToken")
                .verifyComplete();

        verify(userRepository).getUserByEmail("test@mail.com");
        verify(passwordEncoderInputPort).matches("rawPassword", "encodedPassword");
        verify(tokenInputPort).generateToken("test@mail.com", "ADMIN");
    }

    @Test
    void authenticateUserNotFound() {
        when(userRepository.getUserByEmail("notfound@mail.com"))
                .thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.authenticate("notfound@mail.com", "password"))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Usuario o contraseña inválidos"))
                .verify();

        verify(userRepository).getUserByEmail("notfound@mail.com");
        verifyNoInteractions(passwordEncoderInputPort, tokenInputPort);
    }

    @Test
    void authenticateUserWithoutPassword() {
        User user = new User();
        user.setId("2");
        user.setEmail("nopass@mail.com");
        user.setPassword(null);

        when(userRepository.getUserByEmail("nopass@mail.com"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(authUseCase.authenticate("nopass@mail.com", "any"))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Usuario no tiene contraseña"))
                .verify();

        verify(userRepository).getUserByEmail("nopass@mail.com");
        verifyNoInteractions(tokenInputPort);
    }

    @Test
    void authenticateInvalidPassword() {
        User user = new User();
        user.setId("3");
        user.setEmail("wrongpass@mail.com");
        user.setPassword("encodedPassword");

        when(userRepository.getUserByEmail("wrongpass@mail.com"))
                .thenReturn(Mono.just(user));
        when(passwordEncoderInputPort.matches("badPassword", "encodedPassword"))
                .thenReturn(false);

        StepVerifier.create(authUseCase.authenticate("wrongpass@mail.com", "badPassword"))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Usuario o contraseña inválidos"))
                .verify();

        verify(userRepository).getUserByEmail("wrongpass@mail.com");
        verify(passwordEncoderInputPort).matches("badPassword", "encodedPassword");
        verifyNoInteractions(tokenInputPort);
    }
}