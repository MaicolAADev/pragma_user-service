package com.pragma.auth.entrypoints.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth Service API")
                        .version("v1.0")
                        .description("API para el registro de usuarios")
                        .contact(new Contact()
                                .name("Soporte")
                                .email("maicolaroyave10@gmail.com")));
    }
}