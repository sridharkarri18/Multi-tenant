package com.project.multitenant.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Multi-Tenant Feature Flag API",
                version = "1.0",
                description = "API documentation for the Multi-Tenant Feature Flag Management System. It allows super admins to onboard organizations, org admins to manage feature flags, and clients to check feature flags.",
                contact = @Contact(name = "Support", email = "support@example.com")
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter JWT Bearer token **_only_**"
)
public class SwaggerConfig {
}
