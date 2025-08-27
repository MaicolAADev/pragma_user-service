package com.pragma.auth.entrypoints.user.api.v1;

import com.pragma.auth.entrypoints.user.dto.UserRequest;
import com.pragma.auth.model.user.User;
import com.pragma.auth.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UserController {

    private final UserUseCase service;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Permite registrar un nuevo usuario proporcionando sus datos personales básicos.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario creado exitosamente",
                            content = @Content(
                                    schema = @Schema(implementation = User.class),
                                    examples = @ExampleObject(
                                            value = """
                                            {
                                                "id": 10,
                                                "firstName": "Maicol",
                                                "lastName": "Arroyave",
                                                "birthDate": "1990-05-12",
                                                "address": "Calle 123 #45-67",
                                                "phone": "3101234567",
                                                "email": "maicold1414@example.com",
                                                "baseSalary": 15000000
                                            }
                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de validación en los datos de entrada",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(
                                                    name = "Falta campo requerido",
                                                    value = """
                                                    {
                                                        "error": "VALIDATION_ERROR",
                                                        "errors": [
                                                            "baseSalary: baseSalary is required"
                                                        ]
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Formato inválido",
                                                    value = """
                                                    {
                                                        "error": "VALIDATION_ERROR",
                                                        "errors": [
                                                            "email: email must be a valid email address"
                                                        ]
                                                    }
                                                    """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "El email ya está registrado",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                                            {
                                                "error": "DUPLICATE_EMAIL",
                                                "errors": [
                                                    "Email already registered: maicold141@example.com"
                                                ]
                                            }
                                            """
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@Validated @RequestBody UserRequest req) {
        log.trace("POST /api/v1/usuarios - payload email={}", req.getEmail());

        return service.registerUser(
                        req.getFirstName(),
                        req.getLastName(),
                        req.getBirthDate(),
                        req.getAddress(),
                        req.getPhone(),
                        req.getEmail(),
                        req.getBaseSalary()
                )
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                .doOnError(error -> log.error("Error creating user: {}", error.getMessage(), error));
    }

}