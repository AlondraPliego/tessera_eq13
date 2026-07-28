package com.TESSERA.Eq13Tessera.config;

import org.springframework.http.HttpMethod;
import com.TESSERA.Eq13Tessera.config.JwtAuthFilter;
import com.TESSERA.Eq13Tessera.auth.service.UsuarioService;

import org.springframework.security.config.Customizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                // Cualquiera (sin login) puede VER recintos y zonas
                .requestMatchers(HttpMethod.GET, "/api/recintos/mios").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/recintos/**").permitAll()
                // Solo una EMPRESA puede crear/editar/borrar recintos y zonas
                .requestMatchers(HttpMethod.POST, "/api/recintos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.PUT, "/api/recintos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.DELETE, "/api/recintos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.PUT, "/api/zonas/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.DELETE, "/api/zonas/**").hasRole("EMPRESA")
                // Eventos: ver es público, crear/editar/borrar solo EMPRESA
                .requestMatchers(HttpMethod.GET, "/api/eventos/mios").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/eventos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/eventos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.PATCH, "/api/eventos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.DELETE, "/api/eventos/**").hasRole("EMPRESA")
                // Compras: solo CLIENTE compra y cancela; solo ADMIN ve el listado completo
                .requestMatchers(HttpMethod.GET, "/api/compras/mias").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/compras/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/compras").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/compras").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.PATCH, "/api/compras/*/cancelar").hasRole("CLIENTE")
                // Endpoints de prueba de notificaciones: solo ADMIN
                .requestMatchers("/api/notificaciones/**").hasRole("ADMIN")
                // Config pública de seatmap.pro (no es secreta, la usa el frontend)
                .requestMatchers("/api/seatmap/**").permitAll()
                // Subir flyers: solo EMPRESA. Ver la imagen ya subida: cualquiera (público, como cualquier foto en internet).
                .requestMatchers(HttpMethod.POST, "/api/uploads/flyers").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                .anyRequest().authenticated()                   // todo lo demás requiere token
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // sin sesiones, típico de JWT
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(usuarioService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}