package com.TESSERA.Eq13Tessera.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Hace que las imágenes guardadas por FileStorageService sean visibles como
 * si fueran archivos normales de internet, ej:
 * https://tudominio.com/uploads/flyers/9f3a-concierto.jpg
 * <p>
 * Esto NO guarda nada en la base de datos, solo le dice a Spring:
 * "cuando alguien pida algo bajo /uploads/**, ve a buscarlo a esta carpeta del disco".
 */
@Configuration
public class StaticResourceConfig {

    @Value("${app.uploads.dir}")
    private String uploadsDir;

    @Value("${app.uploads.public-path}")
    private String publicPath;

    @Bean
    public WebMvcConfigurer recursosEstaticosConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                String ubicacionDisco = uploadsDir.endsWith("/") ? uploadsDir : uploadsDir + "/";
                registry.addResourceHandler(publicPath + "/**")
                        .addResourceLocations("file:" + ubicacionDisco);
            }
        };
    }
}
