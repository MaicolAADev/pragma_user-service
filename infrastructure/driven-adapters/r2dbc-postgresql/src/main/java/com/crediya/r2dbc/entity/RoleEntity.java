package com.crediya.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import lombok.Data;

@Data
@Table("roles")
public class RoleEntity {
    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}