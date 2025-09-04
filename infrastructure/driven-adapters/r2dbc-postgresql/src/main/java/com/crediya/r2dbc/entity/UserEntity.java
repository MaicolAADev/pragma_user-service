package com.crediya.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("users")
public class UserEntity {
    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("last_name")
    private String lastName;

    @Column("email")
    private String email;

    @Column("born_date")
    private LocalDate bornDate;

    @Column("address")
    private String address;

    @Column("phone_number")
    private String phoneNumber;

    @Column("base_salary")
    private BigDecimal baseSalary;

    @Column("identity_document")
    private String identityDocument;

    @Column("idrol")
    private Long roleId;

    @Column("password_hash")
    private String password;
}
