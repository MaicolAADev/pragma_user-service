package com.crediya.model.role.gateways;

import com.crediya.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> getRoleByName(String name);
}
