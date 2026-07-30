package com.TESSERA.Eq13Tessera.auth.controller;

import com.TESSERA.Eq13Tessera.auth.dto.ActualizarPerfilClienteRequestDTO;
import com.TESSERA.Eq13Tessera.auth.dto.PerfilClienteResponseDTO;
import com.TESSERA.Eq13Tessera.auth.dto.PerfilEmpresaResponseDTO;
import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.auth.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil")
    public ResponseEntity<PerfilClienteResponseDTO> obtenerPerfil(Authentication authentication) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilCliente(obtenerUsuarioId(authentication)));
    }

    @PutMapping("/perfil")
    public ResponseEntity<PerfilClienteResponseDTO> actualizarPerfil(
            @Valid @RequestBody ActualizarPerfilClienteRequestDTO dto, Authentication authentication) {
        return ResponseEntity.ok(
                usuarioService.actualizarPerfilCliente(obtenerUsuarioId(authentication), dto));
    }

    @GetMapping("/perfil-empresa")
    public ResponseEntity<PerfilEmpresaResponseDTO> obtenerPerfilEmpresa(Authentication authentication) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilEmpresa(obtenerUsuarioId(authentication)));
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getUsuarioId();
    }
}
