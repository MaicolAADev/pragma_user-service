package com.crediya.r2dbc;

import com.crediya.model.role.Role;
import com.crediya.model.role.gateways.RoleRepository;
import com.crediya.r2dbc.entity.RoleEntity;
import com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter  extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        String,
        RoleReactiveRepository
        > implements RoleRepository {

    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }
    @Override
    public Mono<Role> getRoleByName(String name) {
        return this.repository.findByName(name)
                .map(entity -> mapper.map(entity, Role.class))
                .onErrorMap(e -> new RuntimeException("Error al consultar rol por nombre", e));
    }
}