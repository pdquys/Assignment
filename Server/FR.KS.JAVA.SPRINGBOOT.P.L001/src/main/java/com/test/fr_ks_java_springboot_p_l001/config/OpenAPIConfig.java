package com.test.fr_ks_java_springboot_p_l001.config;

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
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Quiz Application REST API")
                        .description("""
                                Spring Boot REST API for Quiz Management System
                                
                                **Features:**
                                - JWT Authentication (Login, Register, Refresh Token)
                                - Role-Based Access Control (ADMIN, USER)
                                - CRUD operations for Questions, Quizzes, Users
                                - Exam submission with automatic scoring
                                - Pagination & Sorting support
                                - Soft delete functionality
                                
                                **How to use:**
                                1. Click "Authorize" button (top right)
                                2. Login via /api/v1/auth/login to get access token
                                3. Enter token in format: `Bearer {your-token}`
                                4. Test protected endpoints
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("QuyPD7")
                                .email("QuyPD7@fpt.com"))
                        .license(new License()
                                .name("Educational Project")
                                .url("https://github.com/pdquys/Assignment.git")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT Bearer token from /api/v1/auth/login response")));
    }
}

