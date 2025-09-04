package com.crediya.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("permisos")
public class PermissionEntity {
    @Id
    private Integer id;
    private String name;
    private String description;
}
