package com.crediya.model.user.gateways;

import com.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository {
    Mono<User> getUserByEmail(String email);
    Mono<User> saveUser(User user, Long roleId);
    Mono<User> findByIdentityDocument (String identityDocument);
    Mono<Boolean> existsByEmail(String email);
    Flux<User> findUsersByIdentityDocument(List<String> identities);
}
