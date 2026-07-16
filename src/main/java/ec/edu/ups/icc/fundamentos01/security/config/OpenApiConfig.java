package ec.edu.ups.icc.fundamentos01.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {

        Info info = new Info()
                .title("Fundamentos01 API")
                .description("API REST desarrollada con Spring Boot, Spring Security y JWT (access + refresh token)")
                .version("1.0.0")
                .contact(new Contact()
                        .name("David")
                        .email("desbskull@gmail.com")
                        .url("https://github.com/riiiiii1")
                );

        Server localServer = new Server()
                .url("/api")
                .description("Servidor local");

        SecurityScheme bearerScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("Ingresar el access token generado en /auth/login, sin el prefijo 'Bearer '");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME);

        Components components = new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, bearerScheme);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}