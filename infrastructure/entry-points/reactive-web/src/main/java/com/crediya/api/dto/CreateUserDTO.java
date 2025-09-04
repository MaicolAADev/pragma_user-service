package com.crediya.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateUserDTO {
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    private String email;

    private LocalDate bornDate;
    private String address;
    private String phoneNumber;

    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0", inclusive = true, message = "El salario base debe ser mayor o igual a 0")
    @DecimalMax(value = "15000000", inclusive = true, message = "El salario base debe ser menor o igual a 15,000,000")
    private BigDecimal baseSalary;

    @NotBlank(message = "El documento de identidad es obligatorio")
    private String identityDocument;

    @NotBlank(message = "El rol es obligatorio")
    private String roleName;
    private String password;
}