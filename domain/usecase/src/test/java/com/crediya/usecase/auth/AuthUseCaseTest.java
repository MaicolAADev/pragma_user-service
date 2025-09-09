package com.crediya.usecase.auth;

import com.crediya.model.user.User;
import com.crediya.model.user.gateways.PasswordEncoderInputPort;
import com.crediya.model.user.gateways.TokenInputPort;
import com.crediya.model.user.gateways.UserRepository;
import com.crediya.usecase.exception.ArgumentException;
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
        user.setId("123456");
        user.setEmail("test@mail.com");
        user.setPassword("encodedPassword");
        user.setRoleName("ADMIN");
        user.setIdentityDocument("12345");

        when(userRepository.getUserByEmail("test@mail.com"))
                .thenReturn(Mono.just(user));
        when(passwordEncoderInputPort.matches("rawPassword", "encodedPassword"))
                .thenReturn(true);
        when(tokenInputPort.generateToken("test@mail.com", "ADMIN", "123456", "12345"))
                .thenReturn("mockToken");

        StepVerifier.create(authUseCase.authenticate("test@mail.com", "rawPassword"))
                .expectNext("mockToken")
                .verifyComplete();

        verify(userRepository).getUserByEmail("test@mail.com");
        verify(passwordEncoderInputPort).matches("rawPassword", "encodedPassword");
        verify(tokenInputPort).generateToken("test@mail.com", "ADMIN", "123456", "12345");
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
        user.setRoleName("USER");
        user.setIdentityDocument("12345");

        when(userRepository.getUserByEmail("nopass@mail.com"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(authUseCase.authenticate("nopass@mail.com", "any"))
                .expectErrorMatches(e -> e instanceof ArgumentException &&
                        e.getMessage().equals("Usuario o contraseña inválidos"))
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

    @Test
    void authenticateWithNullEmail() {
        StepVerifier.create(authUseCase.authenticate(null, "password"))
                .expectErrorMatches(e -> e instanceof ArgumentException &&
                        e.getMessage().equals("Usuario o contraseña inválidos"))
                .verify();

        verifyNoInteractions(userRepository, passwordEncoderInputPort, tokenInputPort);
    }

    @Test
    void authenticateWithEmptyEmail() {
        StepVerifier.create(authUseCase.authenticate("", "password"))
                .expectErrorMatches(e -> e instanceof ArgumentException &&
                        e.getMessage().equals("Usuario o contraseña inválidos"))
                .verify();

        verifyNoInteractions(userRepository, passwordEncoderInputPort, tokenInputPort);
    }

    @Test
    void authenticateWithNullPassword() {
        StepVerifier.create(authUseCase.authenticate("test@mail.com", null))
                .expectErrorMatches(e -> e instanceof ArgumentException &&
                        e.getMessage().equals("Usuario o contraseña inválidos"))
                .verify();

        verifyNoInteractions(userRepository, passwordEncoderInputPort, tokenInputPort);
    }

    @Test
    void authenticateWithEmptyPassword() {
        StepVerifier.create(authUseCase.authenticate("test@mail.com", ""))
                .expectErrorMatches(e -> e instanceof ArgumentException &&
                        e.getMessage().equals("Usuario o contraseña inválidos"))
                .verify();

        verifyNoInteractions(userRepository, passwordEncoderInputPort, tokenInputPort);
    }

    @Test
    void authenticateTokenGenerationFails() {
        User user = new User();
        user.setId("6");
        user.setEmail("tokenerror@mail.com");
        user.setPassword("encodedPassword");
        user.setRoleName("ADMIN");
        user.setIdentityDocument("11111");

        when(userRepository.getUserByEmail("tokenerror@mail.com"))
                .thenReturn(Mono.just(user));
        when(passwordEncoderInputPort.matches("password", "encodedPassword"))
                .thenReturn(true);
        when(tokenInputPort.generateToken(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Token generation failed"));

        StepVerifier.create(authUseCase.authenticate("tokenerror@mail.com", "password"))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Error generando token: Token generation failed"))
                .verify();

        verify(userRepository).getUserByEmail("tokenerror@mail.com");
        verify(passwordEncoderInputPort).matches("password", "encodedPassword");
        verify(tokenInputPort).generateToken("tokenerror@mail.com", "ADMIN", "6", "11111");
    }

    @Test
    void authenticatePasswordEncoderThrowsException() {
        User user = new User();
        user.setId("5");
        user.setEmail("error@mail.com");
        user.setPassword("encodedPassword");
        user.setRoleName("USER");
        user.setIdentityDocument("99999");

        when(userRepository.getUserByEmail("error@mail.com"))
                .thenReturn(Mono.just(user));
        when(passwordEncoderInputPort.matches(anyString(), anyString()))
                .thenThrow(new RuntimeException("Encoder error"));

        StepVerifier.create(authUseCase.authenticate("error@mail.com", "password"))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Encoder error"))
                .verify();

        verify(userRepository).getUserByEmail("error@mail.com");
        verify(passwordEncoderInputPort).matches("password", "encodedPassword");
        verifyNoInteractions(tokenInputPort);
    }
}