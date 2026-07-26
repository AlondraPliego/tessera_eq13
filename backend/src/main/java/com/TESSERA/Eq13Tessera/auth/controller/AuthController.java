package com.TESSERA.Eq13Tessera.auth.controller;

import com.TESSERA.Eq13Tessera.auth.dto.*;
import com.TESSERA.Eq13Tessera.auth.service.AuthService;
import com.TESSERA.Eq13Tessera.auth.service.UsuarioService;
import com.TESSERA.Eq13Tessera.config.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UsuarioService usuarioService,
                           AuthService authService,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider) {
        this.usuarioService = usuarioService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO dto) {
        usuarioService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(dto.getEmail(), dto.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);
        String rol = authentication.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(new AuthResponseDTO(token, userDetails.getUsername(), rol));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.invalidarToken(token);
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto) {
        authService.solicitarRecuperacion(dto.getEmail());
        return ResponseEntity.ok("Si el correo existe, se envió un enlace de recuperación");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        authService.restablecerPassword(dto.getToken(), dto.getNuevoPassword());
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}