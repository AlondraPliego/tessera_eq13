package com.TESSERA.Eq13Tessera.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


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
