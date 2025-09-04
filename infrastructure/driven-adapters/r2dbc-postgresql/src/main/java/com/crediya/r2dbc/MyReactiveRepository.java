package com.crediya.r2dbc;

import com.crediya.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<UserEntity> findByEmail(String email);
    Mono<UserEntity> findByIdentityDocument(String identityDocument);
    Mono<Boolean> existsByEmail(String email);
    @Query("SELECT * FROM users WHERE identity_document IN (:identities)")
    Flux<UserEntity> findUsersByIdentityDocument(List<String> identities);
}
