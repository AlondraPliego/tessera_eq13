package com.TESSERA.Eq13Tessera.auth.service;

import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.auth.exception.TokenInvalidoException;
import com.TESSERA.Eq13Tessera.auth.repository.UsuarioRepository;
import com.TESSERA.Eq13Tessera.notificaciones.service.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final Map<String, Long> tokensInvalidados = new ConcurrentHashMap<>();
    private final Map<String, String> tokensRecuperacion = new ConcurrentHashMap<>();

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public AuthService(UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder,
                        MailService mailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public void invalidarToken(String token) {
        tokensInvalidados.put(token, System.currentTimeMillis());
    }

    public boolean estaInvalidado(String token) {
        return tokensInvalidados.containsKey(token);
    }

    public void solicitarRecuperacion(String email) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            String token = UUID.randomUUID().toString();
            tokensRecuperacion.put(token, email);
            mailService.enviarCorreoRecuperacion(email, token);
        });
    }

    public void restablecerPassword(String token, String nuevoPassword) {
        String email = tokensRecuperacion.get(token);
        if (email == null) {
            throw new TokenInvalidoException("El token de recuperación es inválido o ya expiró");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new TokenInvalidoException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(nuevoPassword));
        usuarioRepository.save(usuario);
        tokensRecuperacion.remove(token);
    }
}