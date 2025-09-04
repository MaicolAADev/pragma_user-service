package com.crediya.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public class User {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private LocalDate bornDate;
    private String address;
    private String phoneNumber;
    private BigDecimal baseSalary;
    private String identityDocument;
    private String roleName;
    private String password;

    public User() {
    }

    public User(String id, String name, String lastName, String email, LocalDate bornDate,
                String address, String phoneNumber, BigDecimal baseSalary,
                String identityDocument, String roleName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.bornDate = bornDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.baseSalary = baseSalary;
        this.identityDocument = identityDocument;
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBornDate() {
        return bornDate;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getIdentityDocument() {
        return identityDocument;
    }

    public void setIdentityDocument(String identityDocument) {
        this.identityDocument = identityDocument;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}