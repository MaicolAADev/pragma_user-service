package com.pragma.auth.r2dbc.repository;

import com.pragma.auth.r2dbc.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<Boolean> existsByEmail(String email);
    Mono<UserEntity> findByEmail(String email);
}
