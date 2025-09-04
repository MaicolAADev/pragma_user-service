package com.crediya.r2dbc;

import com.crediya.model.permission.Permission;
import com.crediya.model.permission.gateways.PermissionRepository;
import com.crediya.r2dbc.entity.PermissionEntity;
import com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class PermissionReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Permission,
        PermissionEntity,
        Integer,
        PermissionReactiveRepository
        > implements PermissionRepository {

    public PermissionReactiveRepositoryAdapter(PermissionReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Permission.class));
    }

    @Override
    public Flux<Permission> findByIdUser(Long idUser) {
        return repository.findByUserId(idUser)
                .map(d -> mapper.map(d, Permission.class));
    }
}