package com.crediya.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDTO (String id, String name, String lastName, String email, LocalDate bornDate, String address, String phoneNumber, BigDecimal baseSalary,  String roleName, String identityDocument) {
}