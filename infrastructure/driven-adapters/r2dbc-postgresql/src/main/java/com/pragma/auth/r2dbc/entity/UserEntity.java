package com.pragma.auth.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table("usuarios")
public class UserEntity {
    @Id
    @Column("id")
    private Long id;

    @Column("nombres")
    private String firstName;

    @Column("apellidos")
    private String lastName;

    @Column("fecha_nacimiento")
    private LocalDate birthDate;

    @Column("direccion")
    private String address;

    @Column("telefono")
    private String phone;

    @Column("correo_electronico")
    private String email;

    @Column("salario_base")
    private BigDecimal baseSalary;

    @Column("fecha_creacion")
    private LocalDateTime createdAt;

}
