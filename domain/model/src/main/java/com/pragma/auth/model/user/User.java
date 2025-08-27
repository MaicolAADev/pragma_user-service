package com.pragma.auth.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class User {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;
    private final String address;
    private final String phone;
    private final String email;
    private final BigDecimal baseSalary;

    public User(Long id, String firstName, String lastName, LocalDate birthDate,
                     String address, String phone, String email, BigDecimal baseSalary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.baseSalary = baseSalary;
    }

    public Long getId(){ return id; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }
    public LocalDate getBirthDate(){ return birthDate; }
    public String getAddress(){ return address; }
    public String getPhone(){ return phone; }
    public String getEmail(){ return email; }
    public java.math.BigDecimal getBaseSalary(){ return baseSalary; }
}
