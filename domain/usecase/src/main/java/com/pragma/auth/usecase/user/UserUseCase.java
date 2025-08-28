package com.pragma.auth.usecase.user;

import com.pragma.auth.model.user.User;
import com.pragma.auth.model.user.gateways.UserPersistencePort;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

public class UserUseCase {

    private static final Logger log = Logger.getLogger(UserUseCase.class.getName());

    private final UserPersistencePort persistence;

    public UserUseCase(UserPersistencePort persistence) {
        this.persistence = persistence;
    }

    public Mono<User> registerUser(String firstName,
                                   String lastName,
                                   LocalDate birthDate,
                                   String address,
                                   String phone,
                                   String email,
                                   BigDecimal baseSalary) {

        log.fine("UserUseCase.registerUser - start - email=" + email);

        if (firstName == null || firstName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre no puede ser nulo o vacio"));
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El apellido no puede ser nulo o vacio"));
        }
        if (email == null || email.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El email no puede ser nulo o vacio"));
        }
        if (baseSalary == null) {
            return Mono.error(new IllegalArgumentException("El salario base no puede ser nulo"));
        }
        if (baseSalary.compareTo(BigDecimal.ZERO) < 0 ||
                baseSalary.compareTo(new BigDecimal("15000000")) > 0) {
            return Mono.error(new IllegalArgumentException("Salario fuera de rango"));
        }

        return persistence.existsByEmail(email)
                .flatMap(exists -> {
                    if (exists) {
                        log.warning("Duplicate email detected: " + email);
                        return Mono.error(new DuplicateEmailException(email));
                    }

                    User user = new User(
                            null,
                            firstName,
                            lastName,
                            birthDate,
                            address,
                            phone,
                            email,
                            baseSalary
                    );

                    return persistence.registerUser(user)
                            .doOnSuccess(u -> log.info("User registered id=" + u.getId() + " email=" + u.getEmail()));
                });
    }

    public static class DuplicateEmailException extends RuntimeException {
        public DuplicateEmailException(String email) {
            super("El email ya esta registrado: " + email);
        }
    }
}