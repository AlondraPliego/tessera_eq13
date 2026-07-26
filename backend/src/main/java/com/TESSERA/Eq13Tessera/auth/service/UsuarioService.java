package com.TESSERA.Eq13Tessera.auth.service;

import com.TESSERA.Eq13Tessera.auth.dto.RegisterRequestDTO;
import com.TESSERA.Eq13Tessera.auth.entity.Rol;
import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.auth.exception.RolNoEncontradoException;
import com.TESSERA.Eq13Tessera.auth.exception.UsuarioYaExisteException;
import com.TESSERA.Eq13Tessera.auth.repository.RolRepository;
import com.TESSERA.Eq13Tessera.auth.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                           RolRepository rolRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }

    public Usuario registrar(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new UsuarioYaExisteException("Ya existe una cuenta con ese correo");
        }

        Rol rol = rolRepository.findByNombre(dto.getRolNombre().toUpperCase())
                .orElseThrow(() -> new RolNoEncontradoException(
                        "Rol inválido: " + dto.getRolNombre() + ". Usa EMPRESA o CLIENTE"));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }
}