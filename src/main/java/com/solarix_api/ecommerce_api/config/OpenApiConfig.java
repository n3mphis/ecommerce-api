package com.solarix_api.ecommerce_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Solarix E-commerce API")
                        .version("1.0")
                        .description("API REST para e-commerce con JWT, carrito y checkout")
                )
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .name("Authorization")
                .in(SecurityScheme.In.HEADER);
    }
}
