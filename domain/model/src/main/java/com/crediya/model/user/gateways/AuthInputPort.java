package com.crediya.model.user.gateways;

import reactor.core.publisher.Mono;

public interface AuthInputPort {
    Mono<String> authenticate (String email, String password);
}
