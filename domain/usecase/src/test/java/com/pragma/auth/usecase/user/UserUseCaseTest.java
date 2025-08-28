package com.pragma.auth.usecase.user;

import com.pragma.auth.model.user.User;
import com.pragma.auth.model.user.gateways.UserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserUseCase Unit Tests")
class UserUseCaseTest {

    @Mock
    private UserPersistencePort userPersistencePort;

    private UserUseCase userUseCase;

    private static final LocalDate VALID_BIRTH_DATE = LocalDate.of(1990, 1, 1);
    private static final BigDecimal VALID_SALARY = new BigDecimal("3000000");
    private static final BigDecimal MAX_SALARY = new BigDecimal("15000000");
    private static final BigDecimal NEGATIVE_SALARY = new BigDecimal("-1000");
    private static final BigDecimal EXCESSIVE_SALARY = new BigDecimal("16000000");
    private static final String VALID_EMAIL = "test@pragma.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String ADDRESS = "123 Main St";
    private static final String PHONE = "1234567890";

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(userPersistencePort);
    }

    @Test
    @DisplayName("Should successfully register user with valid data")
    void registerUser_WithValidData_ReturnsRegisteredUser() {
        // Arrange
        User expectedUser = createUser(1L, VALID_EMAIL, VALID_SALARY);

        when(userPersistencePort.existsByEmail(VALID_EMAIL)).thenReturn(Mono.just(false));
        when(userPersistencePort.registerUser(any(User.class))).thenReturn(Mono.just(expectedUser));

        StepVerifier.create(userUseCase.registerUser(
                        FIRST_NAME, LAST_NAME, VALID_BIRTH_DATE, ADDRESS, PHONE, VALID_EMAIL, VALID_SALARY
                ))
                .expectNextMatches(user ->
                        user.getId().equals(1L) &&
                                user.getEmail().equals(VALID_EMAIL) &&
                                user.getBaseSalary().equals(VALID_SALARY)
                )
                .verifyComplete();

        verify(userPersistencePort).existsByEmail(VALID_EMAIL);
        verify(userPersistencePort).registerUser(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when email already exists")
    void registerUser_WithDuplicateEmail_ThrowsException() {
        when(userPersistencePort.existsByEmail(VALID_EMAIL)).thenReturn(Mono.just(true));

        StepVerifier.create(userUseCase.registerUser(
                        FIRST_NAME, LAST_NAME, VALID_BIRTH_DATE, ADDRESS, PHONE, VALID_EMAIL, VALID_SALARY
                ))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(UserUseCase.DuplicateEmailException.class);
                    assertThat(throwable.getMessage()).contains(VALID_EMAIL);
                })
                .verify();

        verify(userPersistencePort).existsByEmail(VALID_EMAIL);
        verify(userPersistencePort, never()).registerUser(any(User.class));
    }

    @Test
    @DisplayName("Should validate null firstName")
    void registerUser_WithNullFirstName_ThrowsValidationException() {
        testValidationException(null, LAST_NAME, VALID_EMAIL, VALID_SALARY,
                "El nombre no puede ser nulo o vacio");
    }

    @Test
    @DisplayName("Should validate empty firstName")
    void registerUser_WithEmptyFirstName_ThrowsValidationException() {
        testValidationException("   ", LAST_NAME, VALID_EMAIL, VALID_SALARY,
                "El nombre no puede ser nulo o vacio");
    }

    @Test
    @DisplayName("Should validate null lastName")
    void registerUser_WithNullLastName_ThrowsValidationException() {
        testValidationException(FIRST_NAME, null, VALID_EMAIL, VALID_SALARY,
                "El apellido no puede ser nulo o vacio");
    }

    @Test
    @DisplayName("Should validate null baseSalary")
    void registerUser_WithNullBaseSalary_ThrowsValidationException() {
        testValidationException(FIRST_NAME, LAST_NAME, VALID_EMAIL, null,
                "El salario base no puede ser nulo");
    }

    @Test
    @DisplayName("Should validate negative baseSalary")
    void registerUser_WithNegativeSalary_ThrowsValidationException() {
        StepVerifier.create(userUseCase.registerUser(
                        FIRST_NAME, LAST_NAME, VALID_BIRTH_DATE, ADDRESS, PHONE, VALID_EMAIL, NEGATIVE_SALARY
                ))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
                    assertThat(throwable.getMessage()).contains("Salario fuera de rango");
                })
                .verify();

        verifyNoInteractions(userPersistencePort);
    }

    @Test
    @DisplayName("Should validate excessive baseSalary")
    void registerUser_WithExcessiveSalary_ThrowsValidationException() {
        StepVerifier.create(userUseCase.registerUser(
                        FIRST_NAME, LAST_NAME, VALID_BIRTH_DATE, ADDRESS, PHONE, VALID_EMAIL, EXCESSIVE_SALARY
                ))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
                    assertThat(throwable.getMessage()).contains("Salario fuera de rango");
                })
                .verify();

        verifyNoInteractions(userPersistencePort);
    }

    @Test
    @DisplayName("Should accept maximum allowed salary")
    void registerUser_WithMaximumSalary_ReturnsUser() {
        User expectedUser = createUser(1L, "max@pragma.com", MAX_SALARY);

        when(userPersistencePort.existsByEmail("max@pragma.com")).thenReturn(Mono.just(false));
        when(userPersistencePort.registerUser(any(User.class))).thenReturn(Mono.just(expectedUser));

        StepVerifier.create(userUseCase.registerUser(
                        FIRST_NAME, LAST_NAME, VALID_BIRTH_DATE, ADDRESS, PHONE, "max@pragma.com", MAX_SALARY
                ))
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userPersistencePort).existsByEmail("max@pragma.com");
        verify(userPersistencePort).registerUser(any(User.class));
    }

    @Test
    @DisplayName("Should accept optional fields as null")
    void registerUser_WithOptionalFieldsNull_ReturnsUser() {
        User expectedUser = new User(1L, FIRST_NAME, LAST_NAME, null, null, null, VALID_EMAIL, VALID_SALARY);

        when(userPersistencePort.existsByEmail(VALID_EMAIL)).thenReturn(Mono.just(false));
        when(userPersistencePort.registerUser(any(User.class))).thenReturn(Mono.just(expectedUser));

        StepVerifier.create(userUseCase.registerUser(
                        FIRST_NAME, LAST_NAME, null, null, null, VALID_EMAIL, VALID_SALARY
                ))
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userPersistencePort).existsByEmail(VALID_EMAIL);
        verify(userPersistencePort).registerUser(any(User.class));
    }

    private void testValidationException(String firstName, String lastName, String email,
                                         BigDecimal salary, String expectedErrorField) {
        StepVerifier.create(userUseCase.registerUser(
                        firstName, lastName, VALID_BIRTH_DATE, ADDRESS, PHONE, email, salary
                ))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
                    assertThat(throwable.getMessage()).contains(expectedErrorField);
                })
                .verify();

        verifyNoInteractions(userPersistencePort);
    }

    private User createUser(Long id, String email, BigDecimal salary) {
        return new User(
                id,
                FIRST_NAME,
                LAST_NAME,
                VALID_BIRTH_DATE,
                ADDRESS,
                PHONE,
                email,
                salary
        );
    }
}