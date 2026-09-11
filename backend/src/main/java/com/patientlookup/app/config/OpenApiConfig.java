package com.patientlookup.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Patient Lookup API",
                version = "1.0.0",
                description = "CRUD API for managing patient records"
        )
)
public class OpenApiConfig {
}