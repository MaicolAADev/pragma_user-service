package com.crediya.r2dbc;

import com.crediya.r2dbc.entity.PermissionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PermissionReactiveRepository extends ReactiveCrudRepository<PermissionEntity, Integer>, ReactiveQueryByExampleExecutor<PermissionEntity> {
    @Query("""
        SELECT p.id, p.name, p.description
        FROM users u
        JOIN roles r ON u.idrol = r.id
        JOIN role_permisos rp ON r.id = rp.role_id
        JOIN permisos p ON rp.permiso_id = p.id
        WHERE u.id = :idUser
    """)
    Flux<PermissionEntity> findByUserId(Long idUser);
}
