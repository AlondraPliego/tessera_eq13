package com.TESSERA.Eq13Tessera.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // En el VPS, agrega ahí la IP o el dominio desde donde sirvas el frontend,
    // ej: CORS_ORIGENES=http://localhost:5173,http://IP_DEL_VPS,https://tudominio.com
    @Value("${app.cors.origenes:http://localhost:5173}")
    private String origenesPermitidos;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(origenesPermitidos.split(","))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}