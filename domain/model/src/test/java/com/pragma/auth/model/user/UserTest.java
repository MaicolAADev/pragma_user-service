package com.pragma.auth.model.user;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userCreation_WithValidData_ShouldCreateInstance() {
        // Arrange
        Long id = 1L;
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "1234567890";
        String email = "john.doe@example.com";
        BigDecimal baseSalary = new BigDecimal("3000000");

        User user = new User(id, firstName, lastName, birthDate, address, phone, email, baseSalary);

        assertAll(
                () -> assertEquals(id, user.getId()),
                () -> assertEquals(firstName, user.getFirstName()),
                () -> assertEquals(lastName, user.getLastName()),
                () -> assertEquals(birthDate, user.getBirthDate()),
                () -> assertEquals(address, user.getAddress()),
                () -> assertEquals(phone, user.getPhone()),
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals(baseSalary, user.getBaseSalary())
        );
    }

    @Test
    void userCreation_WithNullOptionalFields_ShouldCreateInstance() {
        User user = new User(1L, "John", "Doe", null, null, null, "test@example.com", new BigDecimal("1000000"));

        assertAll(
                () -> assertNotNull(user.getId()),
                () -> assertNotNull(user.getFirstName()),
                () -> assertNotNull(user.getLastName()),
                () -> assertNull(user.getBirthDate()),
                () -> assertNull(user.getAddress()),
                () -> assertNull(user.getPhone()),
                () -> assertNotNull(user.getEmail()),
                () -> assertNotNull(user.getBaseSalary())
        );
    }

    @Test
    void userGetters_ShouldReturnCorrectValues() {
        User user = new User(1L, "John", "Doe", LocalDate.of(1990, 1, 1),
                "123 Main St", "1234567890", "john@example.com", new BigDecimal("5000000"));

        assertAll(
                () -> assertEquals(1L, user.getId()),
                () -> assertEquals("John", user.getFirstName()),
                () -> assertEquals("Doe", user.getLastName()),
                () -> assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate()),
                () -> assertEquals("123 Main St", user.getAddress()),
                () -> assertEquals("1234567890", user.getPhone()),
                () -> assertEquals("john@example.com", user.getEmail()),
                () -> assertEquals(new BigDecimal("5000000"), user.getBaseSalary())
        );
    }
}