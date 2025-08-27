package com.pragma.auth.entrypoints.user.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserRequest {

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    private LocalDate birthDate;
    private String address;
    private String phone;

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid email address")
    private String email;

    @NotNull(message = "baseSalary is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "baseSalary must be >= 0")
    @DecimalMax(value = "15000000.0", inclusive = true, message = "baseSalary must be <= 15000000")
    private BigDecimal baseSalary;

}
