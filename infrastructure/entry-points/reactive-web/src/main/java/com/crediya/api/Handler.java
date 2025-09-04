
package com.crediya.api;

import com.crediya.api.dto.AuthDTO;
import com.crediya.api.dto.CreateUserDTO;
import com.crediya.api.dto.IdentitiesRequestDTO;
import com.crediya.api.mapper.UserDTOMapper;
import com.crediya.api.service.AuthService;
import com.crediya.model.user.gateways.UserInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class Handler  {

    private final UserInputPort userInputPort;
    private final UserDTOMapper userDTOMapper;
    private final AuthService authService;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        log.debug("Recibiendo petición para crear usuario");

        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(dto -> log.debug("Payload recibido: {}", dto))
                .map(userDTOMapper::toModel)
                .flatMap(userInputPort::saveUser)
                .map(userDTOMapper::toResponse)
                .flatMap(userResponse -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(userResponse));
    }

    public Mono<ServerResponse> listenFindByEmail(ServerRequest serverRequest) {
        String email = serverRequest.pathVariable("email");
        log.debug("Recibiendo petición para buscar usuario por documento de identidad: {}", email);

        return userInputPort.findByEmail(email)
                .map(userDTOMapper::toResponse)
                .flatMap(userResponse -> ServerResponse.ok().bodyValue(userResponse));
    }

    public Mono<ServerResponse> listenAuthenticate(ServerRequest serverRequest) {
        log.debug("Recibiendo petición para autenticación de usuario");
        return serverRequest.bodyToMono(AuthDTO.class)
                .doOnNext(dto -> log.debug("Payload recibido para autenticación: {}", dto))
                .flatMap(dto -> authService.authenticate(dto.email(), dto.password()))
                .flatMap(token -> ServerResponse.ok().bodyValue(Map.of("token", token)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenFindUsersByIdentityDocument(ServerRequest serverRequest) {
        log.debug("Recibiendo petición para buscar usuarios por documentos de identidad: {}", serverRequest);
        return serverRequest.bodyToFlux(IdentitiesRequestDTO.class)
                .doOnNext(dto -> log.debug("Payload recibido para búsqueda de usuarios: {}", dto))
                .filter(dto -> dto.identities() != null && !dto.identities().isEmpty())
                .flatMap(dto -> userInputPort.findUsersByIdentityDocument(dto.identities()))
                .map(userDTOMapper::toResponse)
                .collectList()
                .flatMap(userResponses -> ServerResponse.ok().bodyValue(userResponses));
    }

}
