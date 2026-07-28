package com.TESSERA.Eq13Tessera.eventos.controller;

import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.eventos.dto.RecintoRequest;
import com.TESSERA.Eq13Tessera.eventos.dto.RecintoResponse;
import com.TESSERA.Eq13Tessera.eventos.service.RecintoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recintos")
public class RecintoController {

    private final RecintoService recintoService;

    public RecintoController(RecintoService recintoService) {
        this.recintoService = recintoService;
    }

    // GET /api/recintos?nombre=teatro&page=0&size=10  -> público, cualquiera puede ver los recintos
    @GetMapping
    public ResponseEntity<Page<RecintoResponse>> listar(
            @RequestParam(required = false) String nombre,
            Pageable pageable) {
        return ResponseEntity.ok(recintoService.listar(nombre, pageable));
    }

    // GET /api/recintos/mios -> solo la empresa dueña ve SUS recintos
    @GetMapping("/mios")
    public ResponseEntity<Page<RecintoResponse>> listarMios(Pageable pageable, Authentication authentication) {
        Long empresaId = obtenerUsuarioId(authentication);
        return ResponseEntity.ok(recintoService.listarPorEmpresa(empresaId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecintoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(recintoService.obtenerPorId(id));
    }

    // Solo usuarios con rol EMPRESA pueden crear recintos (ver SecurityConfig)
    @PostMapping
    public ResponseEntity<RecintoResponse> crear(
            @Valid @RequestBody RecintoRequest dto, Authentication authentication) {
        Long empresaId = obtenerUsuarioId(authentication);
        RecintoResponse creado = recintoService.crear(dto, empresaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecintoResponse> actualizar(
            @PathVariable Long id, @Valid @RequestBody RecintoRequest dto, Authentication authentication) {
        Long empresaId = obtenerUsuarioId(authentication);
        return ResponseEntity.ok(recintoService.actualizar(id, dto, empresaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication authentication) {
        Long empresaId = obtenerUsuarioId(authentication);
        recintoService.eliminar(id, empresaId);
        return ResponseEntity.noContent().build();
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getUsuarioId();
    }
}
