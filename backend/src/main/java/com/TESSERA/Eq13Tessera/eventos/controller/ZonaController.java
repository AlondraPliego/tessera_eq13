package com.TESSERA.Eq13Tessera.eventos.controller;

import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.eventos.dto.ZonaRequest;
import com.TESSERA.Eq13Tessera.eventos.dto.ZonaResponse;
import com.TESSERA.Eq13Tessera.eventos.service.ZonaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ZonaController {

    private final ZonaService zonaService;

    public ZonaController(ZonaService zonaService) {
        this.zonaService = zonaService;
    }

    // GET /api/recintos/5/zonas -> público, cualquiera puede ver las zonas de un recinto
    @GetMapping("/api/recintos/{recintoId}/zonas")
    public ResponseEntity<List<ZonaResponse>> listarPorRecinto(@PathVariable Long recintoId) {
        return ResponseEntity.ok(zonaService.listarPorRecinto(recintoId));
    }

    // POST /api/recintos/5/zonas -> solo la EMPRESA dueña del recinto
    @PostMapping("/api/recintos/{recintoId}/zonas")
    public ResponseEntity<ZonaResponse> crear(
            @PathVariable Long recintoId, @Valid @RequestBody ZonaRequest dto, Authentication authentication) {
        Long empresaId = obtenerUsuarioId(authentication);
        ZonaResponse creada = zonaService.crear(recintoId, dto, empresaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/api/zonas/{zonaId}")
    public ResponseEntity<ZonaResponse> actualizar(
            @PathVariable Long zonaId, @Valid @RequestBody ZonaRequest dto, Authentication authentication) {
        Long empresaId = obtenerUsuarioId(authentication);
        return ResponseEntity.ok(zonaService.actualizar(zonaId, dto, empresaId));
    }

    @DeleteMapping("/api/zonas/{zonaId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long zonaId, Authentication authentication) {
        Long empresaId = obtenerUsuarioId(authentication);
        zonaService.eliminar(zonaId, empresaId);
        return ResponseEntity.noContent().build();
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getUsuarioId();
    }
}
