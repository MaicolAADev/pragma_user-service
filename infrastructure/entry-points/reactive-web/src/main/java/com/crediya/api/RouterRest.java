package com.crediya.api;

import com.crediya.api.config.UserPath;
import com.crediya.api.dto.AuthDTO;
import com.crediya.api.dto.CreateUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springdoc.core.annotations.RouterOperation;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@Tag(name = "User API", description = "Operaciones relacionadas con usuarios")
public class RouterRest {

    private final Handler userHandler;
    private final UserPath userPath;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {"application/json"},
                    method = POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "createUser",
                            summary = "Crear un usuario",
                            description = "Crea un nuevo usuario en el sistema",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Información del usuario a crear",
                                    content = @Content(
                                            schema = @Schema(implementation = CreateUserDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Usuario creado exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = CreateUserDTO.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación en los datos enviados",
                                            content = @Content(schema = @Schema(example = """
                                                    {
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "El correo electrónico no tiene un formato válido",
                                                      "path":  "/api/v1/usuarios"
                                                    }
                                                    """))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(schema = @Schema(example = """
                                                    {
                                                      "status": 500,
                                                      "error": "Internal Server Error",
                                                      "message": "Ocurrió un error inesperado",
                                                      "path":  "/api/v1/usuarios"
                                                    }
                                                    """))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    produces = {"application/json"},
                    method = POST,
                    beanClass = Handler.class,
                    beanMethod = "listenAuthenticate",
                    operation = @Operation(
                            operationId = "authenticateUser",
                            summary = "Autenticar usuario",
                            description = "Valida las credenciales de un usuario y devuelve un token JWT si son correctas.",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Credenciales del usuario",
                                    content = @Content(
                                            schema = @Schema(implementation = AuthDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Autenticación exitosa, retorna un token JWT",
                                            content = @Content(schema = @Schema(example = """
                                                    {
                                                      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                                    }
                                                    """))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Credenciales inválidas o usuario sin contraseña",
                                            content = @Content(schema = @Schema(example = """
                                                    {
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Usuario o contraseña inválidos",
                                                      "path": "/api/v1/authenticate"
                                                    }
                                                    """))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(schema = @Schema(example = """
                                                    {
                                                      "status": 500,
                                                      "error": "Internal Server Error",
                                                      "message": "Ocurrió un error inesperado",
                                                      "path": "/api/v1/authenticate"
                                                    }
                                                    """))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction() {
        return route(POST(userPath.getUsers()), userHandler::listenSaveUser)
                .andRoute(GET(userPath.getUserByEmail()), userHandler::listenFindByEmail)
                .andRoute(POST(userPath.getAuthenticate()), userHandler::listenAuthenticate)
                .andRoute(POST(userPath.getUsersByIdentificationNumbers()), userHandler::listenFindUsersByIdentityDocument)
                .andRoute(GET(userPath.getValidateToken()), userHandler::listenValidateToken);

    }
}