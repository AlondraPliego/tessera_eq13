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
                .requestMatchers(HttpMethod.GET, "/api/usuario/perfil-empresa").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.GET, "/api/recintos/mios").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/recintos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/recintos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.PUT, "/api/recintos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.DELETE, "/api/recintos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.PUT, "/api/zonas/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.DELETE, "/api/zonas/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.GET, "/api/eventos/mios").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/eventos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/eventos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.PATCH, "/api/eventos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.DELETE, "/api/eventos/**").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.POST, "/api/reservas").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.DELETE, "/api/reservas/**").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/compras/mias/boletos").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/compras/mias").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/compras/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/compras").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/compras").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.PATCH, "/api/compras/*/cancelar").hasRole("CLIENTE")
                .requestMatchers("/api/notificaciones/**").hasRole("ADMIN")
                .requestMatchers("/api/seatmap/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/uploads/flyers").hasRole("EMPRESA")
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                .anyRequest().authenticated()           
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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