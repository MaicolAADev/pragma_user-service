package com.pragma.auth.model.user.gateways;

import com.pragma.auth.model.user.User;
import reactor.core.publisher.Mono;

public interface UserPersistencePort {
    Mono<User> registerUser(User user);
    Mono<Boolean> existsByEmail(String email);
}
